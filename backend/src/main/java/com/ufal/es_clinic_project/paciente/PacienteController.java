package com.ufal.es_clinic_project.paciente;


import com.ufal.es_clinic_project.endereco.dto.DadosEndereco;
import com.ufal.es_clinic_project.paciente.dto.DadosAtualizacaoPaciente;
import com.ufal.es_clinic_project.paciente.dto.DadosDatalhePaciente;
import com.ufal.es_clinic_project.paciente.dto.DadosListagemPaciente;
import com.ufal.es_clinic_project.paciente.dto.DadosRegistroPaciente;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("pacientes")
public class PacienteController {
    @Autowired
    PacienteRepository pacienteRepository;

    @PostMapping
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_RECEPCIONISTA"})
    public ResponseEntity registrar(@RequestBody @Valid DadosRegistroPaciente data, UriComponentsBuilder uriBuilder){
        var paciente = new Paciente(data);
        pacienteRepository.save(paciente);
        var uri = uriBuilder.path("Paciente/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDatalhePaciente(paciente));
    }

    @GetMapping
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_RECEPCIONISTA"})
    public ResponseEntity <Page<DadosListagemPaciente>> list(@PageableDefault (size = 10, sort = {"nome"}) Pageable paginacao){
        var pagina = pacienteRepository.findByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(pagina);
    }

    @PutMapping
    @Transactional
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_RECEPCIONISTA"})
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente data){
        var paciente = pacienteRepository.getReferenceById(data.id());
        paciente.atualizarInformacoes(data);
        return ResponseEntity.ok(new DadosDatalhePaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_RECEPCIONISTA"})
    public ResponseEntity delete(@PathVariable Long id){
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.delete();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMINISTRADOR", "ROLE_RECEPCIONISTA"})
    public ResponseEntity detalhesPaciente(@PathVariable Long id){
        var paciente = pacienteRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDatalhePaciente(paciente));
    }

}
