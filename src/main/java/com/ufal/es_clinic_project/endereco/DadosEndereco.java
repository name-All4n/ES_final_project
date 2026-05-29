package com.ufal.es_clinic_project.endereco;

import jakarta.validation.constraints.NotBlank;

public record DadosEndereco(@NotBlank
                            String rua,
                            @NotBlank
                            String bairro,
                            @NotBlank
                            String cep,
                            @NotBlank
                            String cidade,
                            @NotBlank
                            String uf,
                            String complemento,
                            String numero) {
}
