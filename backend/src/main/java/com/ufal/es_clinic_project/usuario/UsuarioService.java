package com.ufal.es_clinic_project.usuario;

import com.ufal.es_clinic_project.usuario.dto.DadosCadastroUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    public Usuario cadastrar(DadosCadastroUsuario data){
        if(usuarioRepository.findByLogin(data.login()) != null){
            throw new IllegalArgumentException("Login já esta em uso: " + data.login());
        }
        var senhaCriptografada = encoder.encode(data.senha());
        var usuario = new Usuario(data.login(), senhaCriptografada, data.papel());
        return  usuarioRepository.save(usuario);
    }
}
