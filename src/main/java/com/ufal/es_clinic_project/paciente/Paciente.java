package com.ufal.es_clinic_project.paciente;

import com.ufal.es_clinic_project.endereco.Endereco;
import com.ufal.es_clinic_project.paciente.dto.DadosRegistroPaciente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    @Embedded
    private Endereco endereco;
    private boolean ativo;

    public Paciente(DadosRegistroPaciente data) {
        this.nome = data.Nome();
        this.email = data.email();
        this.telefone = data.telefone();
        this.cpf = data.cpf();
        this.ativo = true;
        this.endereco = new Endereco(data.endereco());
    }
}
