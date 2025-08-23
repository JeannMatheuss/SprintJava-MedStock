package br.com.medstock.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "unidades")
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String localizacao;

    public Unidade() {
    }

    public Unidade(String nome, String localizacao) {
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }
}