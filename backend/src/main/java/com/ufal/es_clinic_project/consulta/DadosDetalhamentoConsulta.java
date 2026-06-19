package com.ufal.es_clinic_project.consulta;

import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(
        Long id,
        Long medicoId,
        String medicoNome,
        Long pacienteId,
        String pacienteNome,
        LocalDateTime dataHora
) {
    public DadosDetalhamentoConsulta(Consulta c) {
        this(
                c.getId(),
                c.getMedico().getId(),
                c.getMedico().getNome(),
                c.getPaciente().getId(),
                c.getPaciente().getNome(),
                c.getDataHora()
        );
    }
}
