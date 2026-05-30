package com.ufal.es_clinic_project.paciente;


import com.ufal.es_clinic_project.paciente.dto.DadosRegistroPaciente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
public class PacienteController {
    @Autowired
    PacienteRepository pacienteRepository;

    @PostMapping
    public ResponseEntity registrar(@RequestBody @Valid DadosRegistroPaciente data, UriComponentsBuilder uriBuilder){
        var paciente = new Paciente(data);
        pacienteRepository.save(paciente);
        var uri = uriBuilder.path("Paciente/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDatalhePaciente(paciente));
    }
}
