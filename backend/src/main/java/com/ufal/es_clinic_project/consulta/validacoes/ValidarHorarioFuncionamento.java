package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidarHorarioFuncionamento implements ValidacaoAgendamento {
    @Override
    public void validar(DadosAgendamentoConsulta data) {
        var dataHora = data.dataHora();
        var domingo = dataHora.getDayOfWeek() == DayOfWeek.SUNDAY;
        var antesAbertura = dataHora.getHour() < 7;
        var depoisFechamento = dataHora.getHour() >= 18;

        if (domingo || antesAbertura || depoisFechamento) {
            throw new ValidacaoException(
                    "Agendamento fora do horário de funcionamento. " +
                            "A clínica atende de segunda a sábado, das 07h às 18h."
            );
        }
    }
}
