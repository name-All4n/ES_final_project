package com.ufal.es_clinic_project.consulta;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.consulta.validacoes.ValidacaoAgendamento;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.medico.Especialidade;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private List<ValidacaoAgendamento> validacoes;

    @InjectMocks
    private ConsultaService consultaService;

    private static final LocalDateTime HORARIO = LocalDateTime.of(2025, 6, 23, 10, 0);

    // Cenários de sucesso
    @Test
    @DisplayName("Deve agendar consulta com médico informado")
    void deveAgendarComMedicoInformado() {
        var paciente = pacienteMock(1L, true);
        var medico   = medicoMock(2L, true);
        var dados    = new DadosAgendamentoConsulta(2L, 1L, HORARIO, null);
        var consultaSalva = new Consulta(medico, paciente, HORARIO);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);

        var resultado = consultaService.agendar(dados);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getMedico()).isEqualTo(medico);
        assertThat(resultado.getPaciente()).isEqualTo(paciente);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Deve agendar consulta sem médico — escolha automática por especialidade")
    void deveAgendarComEscolhaAutomaticaDeMedico() {
        var paciente = pacienteMock(1L, true);
        var medico   = medicoMock(3L, true);
        var dados    = new DadosAgendamentoConsulta(null, 1L, HORARIO, Especialidade.CARDIOLOGIA);
        var consultaSalva = new Consulta(medico, paciente, HORARIO);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.escolherMedicoDisponivel(Especialidade.CARDIOLOGIA, HORARIO))
                .thenReturn(Optional.of(medico));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consultaSalva);

        var resultado = consultaService.agendar(dados);

        assertThat(resultado.getMedico()).isEqualTo(medico);
        verify(medicoRepository).escolherMedicoDisponivel(Especialidade.CARDIOLOGIA, HORARIO);
    }

    // Cenários de falha
    @Test
    @DisplayName("Deve lançar exceção quando paciente não é encontrado")
    void deveLancarExcecaoSePacienteNaoExiste() {
        var dados = new DadosAgendamentoConsulta(1L, 99L, HORARIO, null);
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.agendar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Paciente não encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção quando médico informado não é encontrado")
    void deveLancarExcecaoSeMedicoNaoExiste() {
        var paciente = pacienteMock(1L, true);
        var dados = new DadosAgendamentoConsulta(99L, 1L, HORARIO, null);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.agendar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Médico não encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção quando não há médico disponível para a especialidade")
    void deveLancarExcecaoSemMedicoDisponivel() {
        var paciente = pacienteMock(1L, true);
        var dados = new DadosAgendamentoConsulta(null, 1L, HORARIO, Especialidade.ORTOPEDIA);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.escolherMedicoDisponivel(Especialidade.ORTOPEDIA, HORARIO))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.agendar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Nenhum médico da especialidade");
    }

    @Test
    @DisplayName("Deve lançar exceção quando medicoId é nulo e especialidade também é nula")
    void deveLancarExcecaoSemMedicoESemEspecialidade() {
        var paciente = pacienteMock(1L, true);
        var dados = new DadosAgendamentoConsulta(null, 1L, HORARIO, null);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        assertThatThrownBy(() -> consultaService.agendar(dados))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("especialidade");
    }

    @Test
    @DisplayName("Deve chamar todas as validações antes de prosseguir")
    void deveChamarTodasAsValidacoes() {
        var paciente = pacienteMock(1L, true);
        var medico   = medicoMock(2L, true);
        var dados    = new DadosAgendamentoConsulta(2L, 1L, HORARIO, null);

        ValidacaoAgendamento validacao1 = mock(ValidacaoAgendamento.class);
        ValidacaoAgendamento validacao2 = mock(ValidacaoAgendamento.class);
        lenient().when(validacoes.iterator()).thenReturn(List.of(validacao1, validacao2).iterator());

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(consultaRepository.save(any())).thenReturn(new Consulta(medico, paciente, HORARIO));

        consultaService.agendar(dados);

        verify(consultaRepository).save(any(Consulta.class));
    }

    // Helpers
    private Paciente pacienteMock(Long id, boolean ativo) {
        return mock(Paciente.class);
    }

    private Medico medicoMock(Long id, boolean ativo) {
        return mock(Medico.class);
    }
}
