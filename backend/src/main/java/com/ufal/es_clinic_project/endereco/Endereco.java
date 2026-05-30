package com.ufal.es_clinic_project.endereco;

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
    private String uf;
    private String comlemento;
    private String numero;

    public Endereco(DadosEndereco data){
        this.cidade = data.cidade();
        this.bairro = data.bairro();
        this.rua = data.rua();
        this.cep = data.cep();
        this.uf = data.uf();
        this.comlemento = data.complemento();
        this.numero = data.numero();
    }
}
