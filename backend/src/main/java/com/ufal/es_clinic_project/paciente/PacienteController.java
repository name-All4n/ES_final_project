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
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseEntity <Page<DadosListagemPaciente>> list(@PageableDefault (page = 10, size = 10, sort = {"nome"}) Pageable paginacao){
        var pagina = pacienteRepository.findByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(pagina);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente data){
        var paciente = pacienteRepository.getReferenceById(data.id());
        paciente.atualizarInformacoes(data);
        return ResponseEntity.ok(new DadosDatalhePaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id){
        var paciente = pacienteRepository.getReferenceById(id);
        paciente.delete();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhesPaciente(@PathVariable Long id){
        var paciente = pacienteRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDatalhePaciente(paciente));
    }

}
