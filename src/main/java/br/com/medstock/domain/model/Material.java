package br.com.medstock.domain.model;

public class Material {
    private int id;
    private String nome;
    private int quantidadeDisponivel;
    private TipoMaterial tipo;

    public Material(int id, String nome, TipoMaterial tipo, int quantidadeDisponivel) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void consumir(int quantidade) {
        if (quantidade <= quantidadeDisponivel) {
            quantidadeDisponivel -= quantidade;
        } else{
            System.out.println("Estoque insufuciente.");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo) {
        this.tipo = tipo;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
