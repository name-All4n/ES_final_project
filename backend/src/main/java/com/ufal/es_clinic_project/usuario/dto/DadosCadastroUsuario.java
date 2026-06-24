package com.ufal.es_clinic_project.usuario.dto;

import com.ufal.es_clinic_project.usuario.Papel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull Papel papel
        ) {
}
