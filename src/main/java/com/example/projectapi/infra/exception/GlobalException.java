package com.example.projectapi.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// Anotação "RestControllerAdvice" que define que a classe é responsável por interceptar
// todas as exceçõse dos controllers.

@RestControllerAdvice
public class GlobalException {

    // ExceptionHandler é um metodo "socorrista", ele especifica o que retornar em um erro
    // especifico. Nesse caso o "MethodArgumentNotValidException" e executa tudo abaixo dele.

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(

            MethodArgumentNotValidException ex, // É o objeto que possui o erro.
            HttpServletRequest request // É o objeto que guarda a requisição, usado para saber a url.
    ) {

        // Cria um mapa para os erros, exemplo : {"nomeDoCampo": "mensagem de erro"}
        Map<String, String> errors = new HashMap<>();

        // "getBindingResult" pega o resultado da validação.
        // "getFieldErrors" pega apenas os erros.
        ex.getBindingResult().getFieldErrors()

                // ".forEach" percorre por todos os erros e adicionar no mapa
                // o nome do erro e a mensagem de erro.
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        // Retornando a resposta.
        // Cria um objeto padrão de resposta com timestamp, errors, path
        Map<String, Object> response = buildResponse(HttpStatus.BAD_REQUEST, "Erro de validacao.", request);

        // Adiciona no campo errors o mapa que criamos
        response.put("errors", errors);

        // Retorna um HTTP
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Anotação que captura o exception EntityNotFound.
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            EntityNotFoundException ex, // Objeto que possui o erro completo.
            HttpServletRequest request // Objeto que possui o request enviado.
    ) {
        // Retorna a mensagem em formato HTTP e o status do erro
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                // Conteudo da mensagem
                // exemplo :
                /*
                {
                    "success": false,
                        "status": 404,
                        "error": "Not Found",
                        "message": "O ID do produto informado não existe no banco.",
                        "path": "/api/produtos/999",
                        "timestamp": "2026-02-17T14:30:00Z"
                }
                */

                .body(buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }


    // Anotação que captura o exception ResponseStatus ( exceção coringa )
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(
            ResponseStatusException ex, // Objeto do erro.
            HttpServletRequest request // Objeto que recebe a url.
    ) {
        // Cria um status HTTP com base no erro.
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        // Retorna o HTTP
        return ResponseEntity.status(status).body(buildResponse(status, ex.getReason(), request));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildResponse(HttpStatus.FORBIDDEN, "Acesso negado.", request));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthentication(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildResponse(HttpStatus.UNAUTHORIZED, "Nao autenticado.", request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(
            org.springframework.dao.DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request));
    }


    // Esse metodo captura todas as exceções que os outros metodos não conseguiram capturar.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(
            RuntimeException ex,
            HttpServletRequest request
    ) {

        // captura o status de erro que deu no objeto exception.
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            // Condição de segurança.
            // Caso o code esteja vazio ele procura no value algum HttpStatus.
            HttpStatus status = responseStatus.code() != HttpStatus.INTERNAL_SERVER_ERROR
                    ? responseStatus.code()
                    : responseStatus.value();
            return ResponseEntity.status(status).body(buildResponse(status, ex.getMessage(), request));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor.", request));
    }

    // Função que cria a resposta
    private Map<String, Object> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Define que a operação falhou.
        response.put("success", false);

        // Pega o número do status (ex: 404).
        response.put("status", status.value());

        // Pega o nome oficial do erro (ex: "Not Found").
        response.put("error", status.getReasonPhrase());

        // A mensagem customizada que você passou.
        response.put("message", message);

        // Pega a URL exata que o usuário tentou acessar.
        response.put("path", request.getRequestURI());

        // Gera o horário exato do erro no padrão ISO (muito importante para logs).
        response.put("timestamp", Instant.now().toString());

        return response;
    }
}
