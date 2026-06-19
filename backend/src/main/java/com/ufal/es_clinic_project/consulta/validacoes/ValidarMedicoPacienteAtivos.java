package com.ufal.es_clinic_project.consulta.validacoes;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.medico.MedicoRepository;
import com.ufal.es_clinic_project.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarMedicoPacienteAtivos implements ValidacaoAgendamento{
    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public void validar(DadosAgendamentoConsulta data) {
        var paciente = pacienteRepository.findById(data.pacienteId())
                .orElseThrow(() -> new ValidacaoException("Paciente não encontrado."));

        if (!paciente.isAtivo()) {
            throw new ValidacaoException("Não é possível agendar consulta para um paciente inativo.");
        }

        if (data.medicoId() != null) {
            var medico = medicoRepository.findById(data.medicoId())
                    .orElseThrow(() -> new ValidacaoException("Médico não encontrado."));

            if (!medico.getAtivo()) {
                throw new ValidacaoException("Não é possível agendar consulta com um médico inativo.");
            }
        }
    }
}
