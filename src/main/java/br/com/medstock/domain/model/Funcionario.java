package br.com.medstock.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "funcionarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_funcionario", discriminatorType = DiscriminatorType.STRING)
public abstract class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    protected String nome;

    @Column(nullable = false, unique = true)
    protected String matricula;

    public Funcionario() {
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }
}