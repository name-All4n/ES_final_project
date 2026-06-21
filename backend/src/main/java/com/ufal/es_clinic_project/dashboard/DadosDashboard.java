package com.ufal.es_clinic_project.dashboard;

import java.util.Map;

public record DadosDashboard(
        long totalConsultas,
        long totalMedicosAtivos,
        long totalPacientesAtivos,
        Map<String, Long> consultasPorEspecialidade
) {
}
