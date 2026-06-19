package com.ufal.es_clinic_project.consulta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    // RF15 — médico já tem consulta no mesmo horário?
    boolean existsByMedicoIdAndDataHora(Long medicoId, LocalDateTime dataHora);

    // RF16 — paciente já tem consulta no mesmo dia?
    @Query("""
            SELECT COUNT(c) > 0 FROM Consulta c
            WHERE c.paciente.id = :pacienteId
            AND c.dataHora >= :inicioDia
            AND c.dataHora < :fimDia
            """)
    boolean existsByPacienteIdAndDataHoraBetween(
            @Param("pacienteId") Long pacienteId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("fimDia") LocalDateTime fimDia
    );
}
