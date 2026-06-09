package com.ufal.es_clinic_project.usuario.dto;

import com.ufal.es_clinic_project.usuario.Papel;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(
        @NotBlank String login,
        @NotBlank String senha,
        @NotBlank Papel papel
        ) {
}
