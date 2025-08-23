package br.com.medstock.domain.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registros_consumo")
public class RegistroDeConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private LocalDate data;

    public RegistroDeConsumo() {
    }

    public RegistroDeConsumo(Material material, Unidade unidade, int quantidade, Funcionario funcionario) {
        this.material = material;
        this.unidade = unidade;
        this.quantidade = quantidade;
        this.funcionario = funcionario;
        this.data = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDate getData() {
        return data;
    }
}