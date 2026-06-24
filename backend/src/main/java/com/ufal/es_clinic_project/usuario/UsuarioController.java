package com.ufal.es_clinic_project.usuario;

import com.ufal.es_clinic_project.usuario.dto.DadosCadastroUsuario;
import com.ufal.es_clinic_project.usuario.dto.DadosDetalhamentoUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Secured("ROLE_ADMINISTRADOR")
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario data, UriComponentsBuilder uriBuilder){
        var usuario = usuarioService.cadastrar(data);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoUsuario(usuario));
    }

    // Devolve os dados do usuário autenticado (id, login e papel). O front usa
    // isso para saber o papel de quem logou — o JWT só carrega o login.
    @GetMapping("/me")
    public ResponseEntity<DadosDetalhamentoUsuario> meusDados(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }
}
