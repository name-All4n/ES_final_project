package com.ufal.es_clinic_project.paciente;

import com.ufal.es_clinic_project.endereco.Endereco;

public record DadosDatalhePaciente(String nome, String email, String telefone, String cpf, Endereco endereco) {
    public DadosDatalhePaciente(Paciente paciente) {
        this(paciente.getNome(), paciente.getEmail(), paciente.getTelefone(),paciente.getCpf(), paciente.getEndereco());
    }
}
