package com.ufal.es_clinic_project.medico.dto;

import com.ufal.es_clinic_project.endereco.dto.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoMedico(@NotNull Long id,
                                     String nome,
                                     String email,
                                     String telefone,
                                     DadosEndereco endereco) {

}
