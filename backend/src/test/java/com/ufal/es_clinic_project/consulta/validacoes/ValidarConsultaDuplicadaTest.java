package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.ConsultaRepository;
import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidarConsultaDuplicadaTest {

    @Mock
    private ConsultaRepository repository;

    @InjectMocks
    private ValidarConsultaDuplicada validador;

    private static final Long PACIENTE_ID = 5L;
    private static final LocalDateTime HORARIO = LocalDateTime.of(2025, 6, 23, 14, 0);

    @Test
    @DisplayName("Deve lançar exceção quando paciente já tem consulta no mesmo dia")
    void deveLancarExcecaoComConsultaDuplicada() {
        var data = new DadosAgendamentoConsulta(1L, PACIENTE_ID, HORARIO, null);

        // Simula que já existe uma consulta para o paciente nesse dia
        when(repository.existsByPacienteIdAndDataHoraBetween(
                eq(PACIENTE_ID), any(LocalDateTime.class), any(LocalDateTime.class))
        ).thenReturn(true);

        assertThatThrownBy(() -> validador.validar(data))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("consulta marcada para este dia");
    }

    @Test
    @DisplayName("Deve passar quando paciente não tem consulta no dia")
    void devePassarQuandoPacienteDisponivel() {
        var data = new DadosAgendamentoConsulta(1L, PACIENTE_ID, HORARIO, null);

        when(repository.existsByPacienteIdAndDataHoraBetween(
                eq(PACIENTE_ID), any(LocalDateTime.class), any(LocalDateTime.class))
        ).thenReturn(false);

        assertThatNoException().isThrownBy(() -> validador.validar(data));
    }

    @Test
    @DisplayName("Deve consultar o repositório com inicio e fim corretos do mesmo dia")
    void deveConsultarIntervaloCorreto() {
        var data = new DadosAgendamentoConsulta(1L, PACIENTE_ID, HORARIO, null);

        var inicioDia = LocalDate.of(2025, 6, 23).atStartOfDay();
        var fimDia = inicioDia.plusDays(1);

        when(repository.existsByPacienteIdAndDataHoraBetween(PACIENTE_ID, inicioDia, fimDia))
                .thenReturn(false);

        assertThatNoException().isThrownBy(() -> validador.validar(data));
    }
}
