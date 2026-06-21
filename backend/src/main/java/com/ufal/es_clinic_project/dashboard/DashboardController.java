package com.ufal.es_clinic_project.dashboard;

import com.ufal.es_clinic_project.consulta.ConsultaRepository;
import com.ufal.es_clinic_project.medico.MedicoRepository;
import com.ufal.es_clinic_project.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("dashboard")
public class DashboardController {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    @Secured("ROLE_ADMINISTRADOR")
    public ResponseEntity<DadosDashboard> dashboard() {
        var totalConsultas = consultaRepository.count();
        var totalMedicosAtivos = medicoRepository.countByAtivoTrue();
        var totalPacientesAtivos = pacienteRepository.countByAtivoTrue();

        var consultasPorEspecialidade = consultaRepository.contarPorEspecialidade().stream()
                .collect(Collectors.toMap(
                        c -> c.getEspecialidade().name(),
                        ConsultaRepository.ConsultasPorEspecialidade::getTotal,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        var dados = new DadosDashboard(
                totalConsultas,
                totalMedicosAtivos,
                totalPacientesAtivos,
                consultasPorEspecialidade
        );

        return ResponseEntity.ok(dados);
    }
}
