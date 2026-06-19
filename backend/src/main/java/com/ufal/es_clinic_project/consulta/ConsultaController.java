package com.ufal.es_clinic_project.consulta;

import com.ufal.es_clinic_project.consulta.dto.DadosAgendamentoConsulta;
import com.ufal.es_clinic_project.consulta.dto.DadosDetalhamentoConsulta;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("consultas")
public class ConsultaController {
    @Autowired
    private ConsultaService consultaService;

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
}
