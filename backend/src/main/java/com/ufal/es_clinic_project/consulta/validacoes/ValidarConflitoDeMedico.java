package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.ConsultaRepository;
import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarConflitoDeMedico implements ValidacaoAgendamento {
    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validar(DadosAgendamentoConsulta data) {
        if (data.medicoId() == null) return; // médico será escolhido depois

        var medicoOcupado = repository.existsByMedicoIdAndDataHora(
                data.medicoId(), data.dataHora());

        if (medicoOcupado) {
            throw new ValidacaoException(
                    "Médico já possui uma consulta agendada para esse horário."
            );
        }

    }
}
