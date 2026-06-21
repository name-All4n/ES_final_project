package com.ufal.es_clinic_project.consulta;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.consulta.dto.DadosDetalhamentoConsulta;
import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.usuario.Usuario;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("consultas")
public class ConsultaController {
    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_RECEPCIONISTA"})
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(
            @RequestBody @Valid DadosAgendamentoConsulta data,
            UriComponentsBuilder uriBuilder) {
        var consulta = consultaService.agendar(data);
        var uri = uriBuilder.path("consultas/{id}").buildAndExpand(consulta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoConsulta(consulta));
    }

    @GetMapping("/agenda-do-dia")
    @Secured("ROLE_MEDICO")
    public ResponseEntity<List<DadosDetalhamentoConsulta>> agendaDoDia(@AuthenticationPrincipal Usuario usuario) {
        if (usuario.getMedico() == null) {
            throw new ValidacaoException("Este usuário não está vinculado a um médico.");
        }

        var inicioDia = LocalDate.now().atStartOfDay();
        var fimDia = inicioDia.plusDays(1);

        var consultas = consultaRepository
                .findByMedicoIdAndDataHoraBetweenOrderByDataHora(usuario.getMedico().getId(), inicioDia, fimDia)
                .stream()
                .map(DadosDetalhamentoConsulta::new)
                .toList();

        return ResponseEntity.ok(consultas);
    }
}
