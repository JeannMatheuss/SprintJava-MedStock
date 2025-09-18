package br.com.medstock.domain.model;


import br.com.medstock.domain.exception.EstoqueException;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "materiais")
public class Material {


    public static final int ESTOQUE_MAXIMO = 50;
    public static final int ESTOQUE_MINIMO = 0;
    public static final int INCREMENTO_DECREMENTO = 10;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false, unique = true)
    public String nome;


    @Column(name = "quantidade_disponivel", nullable = false)
    public int quantidadeDisponivel;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TipoMaterial tipo;
    private int quantidadeAtual;
    private String status;


    public Material() {
    }


    public Material(String nome, int quantidadeDisponivel, TipoMaterial tipo) {
        this.nome = nome;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.tipo = tipo;
    }


    public void adicionarEstoque() {
        if (this.quantidadeDisponivel >= ESTOQUE_MAXIMO) {
            throw new EstoqueException("Estoque máximo de " + ESTOQUE_MAXIMO + " unidades já foi atingido.");
        }
        this.quantidadeDisponivel = Math.min(this.quantidadeDisponivel + INCREMENTO_DECREMENTO, ESTOQUE_MAXIMO);
    }
    
    public void removerEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade a remover deve ser maior que zero.");
        }
        if (this.quantidadeAtual < quantidade) {
            throw new IllegalStateException("Estoque insuficiente. Atual: " + this.quantidadeAtual);
        }
        this.quantidadeAtual -= quantidade;
    }


    public int getQuantidadeDisponivel() {
        return quantidadeAtual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}