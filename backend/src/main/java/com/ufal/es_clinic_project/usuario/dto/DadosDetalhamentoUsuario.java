package com.ufal.es_clinic_project.usuario.dto;

import com.ufal.es_clinic_project.usuario.Papel;
import com.ufal.es_clinic_project.usuario.Usuario;

public record DadosDetalhamentoUsuario(Long id, String login, Papel papel) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getPapel());
    }
}
