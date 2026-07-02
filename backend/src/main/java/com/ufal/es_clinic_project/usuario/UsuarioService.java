package com.ufal.es_clinic_project.usuario;

import com.ufal.es_clinic_project.infra.exception.ValidacaoException;
import com.ufal.es_clinic_project.medico.Medico;
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
        if (data.papel() == Papel.MEDICO) {
            throw new ValidacaoException(
                    "Usuários com papel MEDICO são criados automaticamente ao cadastrar o médico em /medicos.");
        }
        if(usuarioRepository.findByLogin(data.login()) != null){
            throw new ValidacaoException("Login já esta em uso: " + data.login());
        }
        var senhaCriptografada = encoder.encode(data.senha());
        var usuario = new Usuario(data.login(), senhaCriptografada, data.papel());
        return  usuarioRepository.save(usuario);
    }

    public Usuario cadastrarParaMedico(Medico medico, String senhaPlano) {
        if (usuarioRepository.findByLogin(medico.getEmail()) != null) {
            throw new ValidacaoException("Já existe um usuário com este login: " + medico.getEmail());
        }
        var senhaCriptografada = encoder.encode(senhaPlano);
        var usuario = new Usuario(medico.getEmail(), senhaCriptografada, Papel.MEDICO, medico);
        return usuarioRepository.save(usuario);
    }
}
