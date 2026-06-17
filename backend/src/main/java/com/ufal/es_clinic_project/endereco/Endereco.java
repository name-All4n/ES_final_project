package com.ufal.es_clinic_project.endereco;

import com.ufal.es_clinic_project.endereco.dto.DadosEndereco;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Endereco {
    private String cidade;
    private String bairro;
    private String rua;
    private String cep;
    @Column(length = 2)
    private String uf;
    private String complemento;
    private String numero;

    public Endereco(DadosEndereco data){
        this.cidade = data.cidade();
        this.bairro = data.bairro();
        this.rua = data.rua();
        this.cep = data.cep();
        this.uf = data.uf();
        this.complemento = data.complemento();
        this.numero = data.numero();
    }

    public void atualizarInformacoes(DadosEndereco data) {
        if(data.cidade() != null) {
            this.cidade = data.cidade();
        }
        if(data.bairro() != null) {
            this.bairro = data.bairro();
        }
        if(data.rua() != null) {
            this.rua = data.rua();
        }
        if(data.cep() != null) {
            this.cep = data.cep();
        }
        if(data.uf() != null) {
            this.uf = data.uf();
        }
        if(data.complemento() != null) {
            this.complemento = data.complemento();
        }
        if(data.numero() != null) {
            this.numero = data.numero();
        }
    }
}
