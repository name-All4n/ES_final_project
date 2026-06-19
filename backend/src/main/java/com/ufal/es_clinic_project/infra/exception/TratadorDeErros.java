package com.ufal.es_clinic_project.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {
    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosErro> tratarValidacaoException(ValidacaoException ex) {
        return ResponseEntity.badRequest().body(new DadosErro(ex.getMessage()));
    }
    public record DadosErro(String mensagem) {}
}
