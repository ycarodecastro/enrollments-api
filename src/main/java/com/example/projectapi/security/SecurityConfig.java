package com.example.projectapi.security;

import com.example.projectapi.domain.user.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Value("${app.security.h2-console-enabled:false}")
    private boolean h2ConsoleEnabled;

    // List de todos os url's que sao autorizados por padrao
    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        if (h2ConsoleEnabled) {
            httpSecurity.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        }

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/error").permitAll()
                            .requestMatchers(AUTH_WHITELIST).permitAll();

                    // H2 Console (dentro do authorizeHttpRequests e ANTES do anyRequest)
                    if (h2ConsoleEnabled) {
                        authorize.requestMatchers("/h2-console/**").permitAll();
                    }

                    authorize

                            // Auth
                            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()

                            // Students
                            .requestMatchers(HttpMethod.GET, "/api/students/profile").hasRole(UserRole.ALUNO.toString())
                            .requestMatchers(HttpMethod.POST, "/api/students").permitAll()
                            .requestMatchers(HttpMethod.PUT, "/api/students/me").hasRole(UserRole.ALUNO.toString())
                            .requestMatchers(HttpMethod.DELETE, "/api/students/me").hasRole(UserRole.ALUNO.toString())

                            // Schools
                            .requestMatchers(HttpMethod.GET, "/api/schools/profile").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.GET, "/api/schools/students").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.POST, "/api/schools").permitAll()
                            .requestMatchers(HttpMethod.PUT, "/api/schools/me").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.DELETE, "/api/schools/me").hasRole(UserRole.ESCOLA.toString())

                            // Offers
                            .requestMatchers(HttpMethod.GET, "/api/offers").hasRole(UserRole.ALUNO.toString())
                            .requestMatchers(HttpMethod.POST, "/api/offers").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.PUT, "/api/offers/**").hasRole(UserRole.ESCOLA.toString())

                            // Inscribes
                            .requestMatchers(HttpMethod.GET, "/api/inscribes").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.POST, "/api/inscribes").hasRole(UserRole.ALUNO.toString())
                            .requestMatchers(HttpMethod.PUT, "/api/inscribes/**").hasRole(UserRole.ESCOLA.toString())

                            // Transcripts
                            .requestMatchers(HttpMethod.GET, "/api/transcripts/current").hasRole(UserRole.ALUNO.toString())
                            .requestMatchers(HttpMethod.GET, "/api/transcripts").hasRole(UserRole.ALUNO.toString())
                            .requestMatchers(HttpMethod.GET, "/api/transcripts/student/**").hasRole(UserRole.ESCOLA.toString())

                            // Subjects
                            .requestMatchers(HttpMethod.GET, "/api/subjects").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.POST, "/api/subjects").hasRole(UserRole.ESCOLA.toString())

                            // Grades
                            .requestMatchers(HttpMethod.POST, "/api/transcripts/*/grades").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.PUT, "/api/transcripts/*/grades/**").hasRole(UserRole.ESCOLA.toString())
                            .requestMatchers(HttpMethod.DELETE, "/api/transcripts/*/grades/**").hasRole(UserRole.ESCOLA.toString())

                            .anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
