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

    public Material() {
    }

    public void adicionarEstoque() {
        if (this.quantidadeDisponivel >= ESTOQUE_MAXIMO) {
            throw new EstoqueException("Estoque máximo de " + ESTOQUE_MAXIMO + " unidades já foi atingido.");
        }
        this.quantidadeDisponivel = Math.min(this.quantidadeDisponivel + INCREMENTO_DECREMENTO, ESTOQUE_MAXIMO);
    }

    public void removerEstoque() {
        if (this.quantidadeDisponivel <= ESTOQUE_MINIMO) {
            throw new EstoqueException("Estoque já está no mínimo de " + ESTOQUE_MINIMO + " unidades.");
        }
        this.quantidadeDisponivel = Math.max(this.quantidadeDisponivel - INCREMENTO_DECREMENTO, ESTOQUE_MINIMO);
    }

    public NivelEstoqueStatus getStatus() {
        return NivelEstoqueStatus.fromQuantidade(this.quantidadeDisponivel);
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