package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.ConsultaRepository;
import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarConflitoDeMedicoTest {

    @Mock
    private ConsultaRepository repository;

    @InjectMocks
    private ValidarConflitoDeMedico validador;

    private static final LocalDateTime HORARIO = LocalDateTime.of(2025, 6, 23, 10, 0);

    @Test
    @DisplayName("Deve lançar exceção quando médico já tem consulta no mesmo horário")
    void deveLancarExcecaoQuandoMedicoOcupado() {
        var data = new DadosAgendamentoConsulta(1L, 2L, HORARIO, null);
        when(repository.existsByMedicoIdAndDataHora(1L, HORARIO)).thenReturn(true);

        assertThatThrownBy(() -> validador.validar(data))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Médico já possui uma consulta");
    }

    @Test
    @DisplayName("Deve passar quando médico está disponível no horário")
    void devePassarQuandoMedicoDisponivel() {
        var data = new DadosAgendamentoConsulta(1L, 2L, HORARIO, null);
        when(repository.existsByMedicoIdAndDataHora(1L, HORARIO)).thenReturn(false);

        assertThatNoException().isThrownBy(() -> validador.validar(data));
        verify(repository).existsByMedicoIdAndDataHora(1L, HORARIO);
    }

    @Test
    @DisplayName("Deve ignorar validação quando medicoId é nulo (escolha automática)")
    void deveIgnorarValidacaoSemMedicoId() {
        // medicoId null = médico será escolhido automaticamente depois
        var data = new DadosAgendamentoConsulta(null, 2L, HORARIO, Especialidade.CARDIOLOGIA);

        assertThatNoException().isThrownBy(() -> validador.validar(data));
        // repositório nunca deve ser consultado nesse caso
        verifyNoInteractions(repository);
    }
}
