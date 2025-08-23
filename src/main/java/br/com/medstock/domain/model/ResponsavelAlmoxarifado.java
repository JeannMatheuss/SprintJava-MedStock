package br.com.medstock.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("RESPONSAVEL_ALMOXARIFADO")
public class ResponsavelAlmoxarifado extends Funcionario {

    public ResponsavelAlmoxarifado() {
    }

    public ResponsavelAlmoxarifado(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }
}