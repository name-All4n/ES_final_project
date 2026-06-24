package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.medico.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidarHorarioFuncionamentoTest {

    private final ValidarHorarioFuncionamento validador = new ValidarHorarioFuncionamento();

    // ----- horários VÁLIDOS -----
    @Test
    @DisplayName("Deve aceitar consulta em dia útil dentro do horário de funcionamento")
    void devePemitirConsultaSegundaFeira() {
        // Segunda-feira às 09h
        var data = dadosPara(LocalDateTime.of(2025, 6, 23, 9, 0)); // segunda
        assertThatNoException().isThrownBy(() -> validador.validar(data));
    }

    @Test
    @DisplayName("Deve aceitar consulta no horário de abertura (07h)")
    void devePermitirHorarioAbertura() {
        var data = dadosPara(LocalDateTime.of(2025, 6, 23, 7, 0)); // segunda 07h
        assertThatNoException().isThrownBy(() -> validador.validar(data));
    }

    @Test
    @DisplayName("Deve aceitar consulta no sábado dentro do horário")
    void devePermitirSabado() {
        var data = dadosPara(LocalDateTime.of(2025, 6, 28, 10, 0)); // sábado 10h
        assertThatNoException().isThrownBy(() -> validador.validar(data));
    }

    @Test
    @DisplayName("Deve aceitar consulta às 17h (último horário válido)")
    void devePermitir17h() {
        var data = dadosPara(LocalDateTime.of(2025, 6, 23, 17, 0));
        assertThatNoException().isThrownBy(() -> validador.validar(data));
    }

    // ----- horários INVÁLIDOS -----

    @Test
    @DisplayName("Deve rejeitar consulta no domingo")
    void deveRejeitarDomingo() {
        var data = dadosPara(LocalDateTime.of(2025, 6, 29, 10, 0)); // domingo
        assertThatThrownBy(() -> validador.validar(data))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("horário de funcionamento");
    }

    @Test
    @DisplayName("Deve rejeitar consulta antes das 07h")
    void deveRejeitarAntesAbertura() {
        var data = dadosPara(LocalDateTime.of(2025, 6, 23, 6, 59));
        assertThatThrownBy(() -> validador.validar(data))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("horário de funcionamento");
    }

    @Test
    @DisplayName("Deve rejeitar consulta às 18h (após fechamento)")
    void deveRejeitarApos18h() {
        var data = dadosPara(LocalDateTime.of(2025, 6, 23, 18, 0));
        assertThatThrownBy(() -> validador.validar(data))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("horário de funcionamento");
    }

    @Test
    @DisplayName("Deve rejeitar consulta à meia-noite")
    void deveRejeitarMeiaNoite() {
        var data = dadosPara(LocalDateTime.of(2025, 6, 23, 0, 0));
        assertThatThrownBy(() -> validador.validar(data))
                .isInstanceOf(ValidacaoException.class);
    }

    private DadosAgendamentoConsulta dadosPara(LocalDateTime dataHora) {
        return new DadosAgendamentoConsulta(1L, 1L, dataHora, Especialidade.CARDIOLOGIA);
    }
}
