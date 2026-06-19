package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.ConsultaRepository;
import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarConsultaDuplicada implements ValidacaoAgendamento{
    @Autowired
    private ConsultaRepository repository;

    @Override
    public void validar(DadosAgendamentoConsulta data) {
        var inicioDia = data.dataHora().toLocalDate().atStartOfDay();
        var fimDia = inicioDia.plusDays(1);

        var pacienteJaTemConsultaNoDia = repository.existsByPacienteIdAndDataHoraBetween(
                data.pacienteId(), inicioDia, fimDia);

        if (pacienteJaTemConsultaNoDia) {
            throw new ValidacaoException(
                    "Paciente já possui uma consulta marcada para este dia."
            );
        }
    }
}
