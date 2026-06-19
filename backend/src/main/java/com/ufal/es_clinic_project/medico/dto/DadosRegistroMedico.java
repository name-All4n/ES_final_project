package com.ufal.es_clinic_project.medico.dto;

import com.ufal.es_clinic_project.endereco.dto.DadosEndereco;
import com.ufal.es_clinic_project.medico.Especialidade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosRegistroMedico(@NotBlank String nome,
                                  @NotBlank @Email String email,
                                  @NotBlank String senha,
                                  @NotBlank String telefone,
                                  @NotBlank String crm,
                                  @NotNull Especialidade especialidade,
                                  @NotNull @Valid DadosEndereco endereco) {
}
