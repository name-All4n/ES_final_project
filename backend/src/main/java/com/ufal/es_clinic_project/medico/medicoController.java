package com.ufal.es_clinic_project.medico;

import com.ufal.es_clinic_project.medico.dto.DadosAtualizacaoMedico;
import com.ufal.es_clinic_project.medico.dto.DadosDetalhesMedico;
import com.ufal.es_clinic_project.medico.dto.DadosListagemMedicos;
import com.ufal.es_clinic_project.medico.dto.DadosRegistroMedico;
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
@RequestMapping("medicos")
public class medicoController {
    @Autowired
    private MedicoRepository medicoRepository;

    @Transactional
    @PostMapping
    public ResponseEntity registrarMedico(@RequestBody @Valid DadosRegistroMedico data, UriComponentsBuilder uriBuilder){
        var medico = new Medico(data);
        medicoRepository.save(medico);
        var uri = uriBuilder.path("medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhesMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedicos>> list(@PageableDefault(size=10, sort={"nome"}) Pageable paginacao) {
        var pagina = medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);
        return ResponseEntity.ok(pagina);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico data) {
        var medico = medicoRepository.getReferenceById(data.id());
        medico.atualizarInformacoes(data);
        return ResponseEntity.ok(new DadosDetalhesMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteMedico(@PathVariable Long id){
        var medico = medicoRepository.getReferenceById(id);
        medico.delete();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhesMedico(@PathVariable Long id){
        var medico = medicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhesMedico(medico));
    }
}
