package com.ufal.es_clinic_project.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico,Long> {
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
            SELECT m FROM Medico m
            WHERE m.ativo = true
            AND m.especialidade = :especialidade
            AND m.id NOT IN (
                SELECT c.medico.id FROM Consulta c WHERE c.dataHora = :dataHora
            )
            ORDER BY m.id
            """)
    Optional<Medico> escolherMedicoDisponivel(
            @Param("especialidade") Especialidade especialidade,
            @Param("dataHora") LocalDateTime dataHora
    );
}
