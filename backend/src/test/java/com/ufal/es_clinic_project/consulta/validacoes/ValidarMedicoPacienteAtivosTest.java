package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.medico.Medico;
import com.ufal.es_clinic_project.medico.MedicoRepository;
import com.ufal.es_clinic_project.paciente.Paciente;
import com.ufal.es_clinic_project.paciente.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarMedicoPacienteAtivosTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private ValidarMedicoPacienteAtivos validador;

    private static final LocalDateTime HORARIO = LocalDateTime.of(2025, 6, 23, 10, 0);

    @Test
    @DisplayName("Deve passar quando paciente e médico estão ativos")
    void devePassarQuandoAmbosAtivos() {
        var paciente = pacienteMock(true);
        var medico   = medicoMock(true);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));

        var dados = new DadosAgendamentoConsulta(2L, 1L, HORARIO, null);
        assertThatNoException().isThrownBy(() -> validador.validar(dados));
    }

    @Test
    @DisplayName("Deve lançar exceção quando paciente está inativo")
    void deveLancarExcecaoPacienteInativo() {
        var paciente = pacienteMock(false);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        var dados = new DadosAgendamentoConsulta(2L, 1L, HORARIO, null);
        assertThatThrownBy(() -> validador.validar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("paciente inativo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando médico está inativo")
    void deveLancarExcecaoMedicoInativo() {
        var paciente = pacienteMock(true);
        var medico   = medicoMock(false);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));

        var dados = new DadosAgendamentoConsulta(2L, 1L, HORARIO, null);
        assertThatThrownBy(() -> validador.validar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("médico inativo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando paciente não é encontrado")
    void deveLancarExcecaoPacienteNaoEncontrado() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        var dados = new DadosAgendamentoConsulta(2L, 99L, HORARIO, null);
        assertThatThrownBy(() -> validador.validar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Paciente não encontrado");
    }

    @Test
    @DisplayName("Não deve validar médico quando medicoId é nulo")
    void naoDeveValidarMedicoSemId() {
        var paciente = pacienteMock(true);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        var dados = new DadosAgendamentoConsulta(null, 1L, HORARIO, null);
        assertThatNoException().isThrownBy(() -> validador.validar(dados));
        verifyNoInteractions(medicoRepository);
    }

    // helpers
    private Paciente pacienteMock(boolean ativo) {
        var p = mock(Paciente.class);
        when(p.isAtivo()).thenReturn(ativo);
        return p;
    }

    private Medico medicoMock(boolean ativo) {
        var m = mock(Medico.class);
        when(m.getAtivo()).thenReturn(ativo);
        return m;
    }
}
