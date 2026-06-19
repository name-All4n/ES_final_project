package com.ufal.es_clinic_project.consulta;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.consulta.validacoes.ValidacaoAgendamento;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.medico.Medico;
import com.ufal.es_clinic_project.medico.MedicoRepository;
import com.ufal.es_clinic_project.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidacaoAgendamento> validacoes;

    public Consulta agendar(DadosAgendamentoConsulta data) {
        validacoes.forEach(v -> v.validar(data));

        var paciente = pacienteRepository.findById(data.pacienteId())
                .orElseThrow(() -> new ValidacaoException("Paciente não encontrado."));

        Medico medico;
        if (data.medicoId() != null) {
            medico = medicoRepository.findById(data.medicoId())
                    .orElseThrow(() -> new ValidacaoException("Médico não encontrado."));
        } else { if (data.especialidade() == null) {
            throw new ValidacaoException(
                    "Informe o médico desejado ou a especialidade para escolha automática.");
        }
            medico = medicoRepository
                    .escolherMedicoDisponivel(data.especialidade(), data.dataHora())
                    .orElseThrow(() -> new ValidacaoException(
                            "Nenhum médico da especialidade " + data.especialidade() +
                                    " está disponível nesse horário."));
        }

        var consulta = new Consulta(medico, paciente, data.dataHora());
        return consultaRepository.save(consulta);
    }
}
