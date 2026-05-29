package com.ufal.es_clinic_project.paciente.dto;

import com.ufal.es_clinic_project.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosRegistroPaciente(@NotBlank
                                    String Nome,
                                    @NotBlank
                                    String email,
                                    @NotBlank
                                    String telefone,
                                    @NotBlank
                                    String cpf,
                                    @NotNull @Valid
                                    DadosEndereco endereco) {
}
