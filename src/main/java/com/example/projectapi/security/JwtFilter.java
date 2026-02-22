package com.example.projectapi.security;

import com.example.projectapi.domain.user.model.UserEntity;
import com.example.projectapi.domain.user.repository.UserRepository;
import com.example.projectapi.infra.redis.RedisJwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter é uma classe que garante que o filtro rode antes de cada
// request.
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RedisJwtService redisJwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // Objeto que recebe todas as informações do request (TomCat - server)
            @NonNull HttpServletResponse response, // Objeto que envia a resposta para o cliente (TomCat - server)
            @NonNull FilterChain filterChain // Objeto que controla a fila de segurança (TomCat - server)
    ) throws ServletException, IOException {

        // Pega a url do request
        String path = request.getRequestURI();

        // Se a url for a de login ou a de refresh ele pode passar sem o token
        if ("/api/auth/login".equals(path) || "/api/auth/refresh".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Pega o token no header authorization
        String authHeader = request.getHeader("Authorization");

        // Se o token for diferente de nulo e começar com Bearer ele pega o token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Ele valida o token

            if (!jwtUtil.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Ele checa se o token não está na blackList do Redis
            String jti = jwtUtil.getJti(token);
            if (redisJwtService.isBlacklisted(jti)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Pega o user ID e a role
            Long userId = jwtUtil.getUserId(token);
            String role = jwtUtil.getRole(token);

            // Confere se o usuario existe
            UserEntity user = userRepository.findById(userId).orElse(null);

            // Se for diferente de null
            if (user != null) {
                // Objeto do tipo Authority para indicar ao Spring que o usuario está autorizado
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                // Ele cria uma identidade interna no servidor. Funciona como um cartão de acesso
                // temporario ( é onde os dados do usuario estará salvo na api )
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(authority)
                        );

                // Indexa todos os dados extra ao usuario ( IP, ID)
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Autentica o usuario de fato para o sistema
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

}
