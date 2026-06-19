package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;

public interface ValidacaoAgendamento {
    void validar(DadosAgendamentoConsulta data);
}
