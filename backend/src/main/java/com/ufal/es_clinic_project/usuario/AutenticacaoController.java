package com.ufal.es_clinic_project.usuario;

import com.ufal.es_clinic_project.infra.security.TokenService;
import com.ufal.es_clinic_project.usuario.dto.DadosAutenticacao;
import com.ufal.es_clinic_project.usuario.dto.DadosToeknJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DadosToeknJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao data) {
        System.out.println("CHEGOU NO CONTROLLER");

        var authToken = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var authentication = authenticationManager.authenticate(authToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosToeknJWT(tokenJWT));
    }
}
