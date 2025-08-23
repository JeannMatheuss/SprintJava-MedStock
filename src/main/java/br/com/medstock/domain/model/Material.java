package br.com.medstock.domain.model;

import br.com.medstock.domain.exception.EstoqueInsuficienteException;

import javax.persistence.*;

@Entity
@Table(name = "materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "quantidade_disponivel", nullable = false)
    private int quantidadeDisponivel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMaterial tipo;

    public Material() {
    }

    public Material(String nome, TipoMaterial tipo, int quantidadeDisponivel) {
        this.nome = nome;
        this.tipo = tipo;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void consumir(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a ser consumida deve ser positiva.");
        }
        if (quantidade > this.quantidadeDisponivel) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o material '" + nome + "'. Solicitado: " + quantidade + ", Disponível: " + this.quantidadeDisponivel
            );
        }
        this.quantidadeDisponivel -= quantidade;
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a ser adicionada deve ser positiva.");
        }
        this.quantidadeDisponivel += quantidade;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public TipoMaterial getTipo() {
        return tipo;
    }
}