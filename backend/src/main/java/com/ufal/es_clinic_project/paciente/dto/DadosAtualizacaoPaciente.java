package com.ufal.es_clinic_project.paciente.dto;

import com.ufal.es_clinic_project.endereco.Endereco;
import com.ufal.es_clinic_project.endereco.dto.DadosEndereco;
import jakarta.validation.Valid;

public record DadosAtualizacaoPaciente(Long id,
                                       String nome,
                                       String email,
                                       String telefone,
                                       @Valid DadosEndereco endereco) {
}
