package com.ufal.es_clinic_project.medico.dto;

import com.ufal.es_clinic_project.endereco.Endereco;
import com.ufal.es_clinic_project.medico.Especialidade;
import com.ufal.es_clinic_project.medico.Medico;

public record DadosDetalhesMedico(Long id, String nome, String email, String crm, String telefone, Especialidade especialidade, Endereco endereco) {
    public DadosDetalhesMedico(Medico medico){
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getTelefone(), medico.getEspecialidade(), medico.getEndereco());
    }
}
