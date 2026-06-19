package com.ufal.es_clinic_project.consulta;

import com.ufal.es_clinic_project.medico.Especialidade;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(
        Long medicoId,
        @NotNull Long pacienteId,
        @NotNull @Future LocalDateTime dataHora,
        Especialidade especialidade) {
}
