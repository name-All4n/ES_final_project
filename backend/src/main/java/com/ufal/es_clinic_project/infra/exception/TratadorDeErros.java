package com.ufal.es_clinic_project.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosErro> tratarValidacaoException(ValidacaoException ex) {
        return ResponseEntity.badRequest().body(new DadosErro(ex.getMessage()));
    }

    // Falhas de validação de campos (@Valid nos DTOs): campos em branco, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DadosErro> tratarErroValidacaoCampos(MethodArgumentNotValidException ex) {
        var mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new DadosErro(mensagem));
    }

    // Login com senha/usuário incorretos.
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DadosErro> tratarBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new DadosErro("Email ou senha incorretos."));
    }

    // Usuário autenticado mas sem permissão para a operação (@Secured).
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<DadosErro> tratarAccessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new DadosErro("Você não tem permissão para esta operação."));
    }

    public record DadosErro(String mensagem) {}
}
