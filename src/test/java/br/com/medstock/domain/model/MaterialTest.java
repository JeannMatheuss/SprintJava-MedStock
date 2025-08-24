package br.com.medstock.domain.model;

import br.com.medstock.domain.exception.EstoqueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialTest {

    private Material material;

    @BeforeEach
    void setUp() {
        material = new Material();
        material.nome = "Vacina AstraZeneca";
        material.tipo = TipoMaterial.DESCARTAVEL;
        material.quantidadeDisponivel = 20;
    }

    @Test
    void testAdicionarEstoqueNormal() {
        material.adicionarEstoque();
        assertEquals(30, material.getQuantidadeDisponivel());
    }

    @Test
    void testAdicionarEstoqueNoLimiteMaximo() {
        material.quantidadeDisponivel = Material.ESTOQUE_MAXIMO;
        EstoqueException exception = assertThrows(EstoqueException.class, () -> material.adicionarEstoque());
        assertEquals("Estoque máximo de 50 unidades já foi atingido.", exception.getMessage());
    }

    @Test
    void testRemoverEstoqueNormal() {
        material.removerEstoque();
        assertEquals(10, material.getQuantidadeDisponivel());
    }

    @Test
    void testRemoverEstoqueNoLimiteMinimo() {
        material.quantidadeDisponivel = Material.ESTOQUE_MINIMO;
        EstoqueException exception = assertThrows(EstoqueException.class, () -> material.removerEstoque());
        assertEquals("Estoque já está no mínimo de 0 unidades.", exception.getMessage());
    }

    @Test
    void testGetStatus() {
        material.quantidadeDisponivel = 5;
        assertEquals(NivelEstoqueStatus.CRITICA, material.getStatus());

        material.quantidadeDisponivel = 15;
        assertEquals(NivelEstoqueStatus.PREOCUPANTE, material.getStatus());

        material.quantidadeDisponivel = 30;
        assertEquals(NivelEstoqueStatus.OK, material.getStatus());

        material.quantidadeDisponivel = 0;
        assertEquals(NivelEstoqueStatus.EXTREMA, material.getStatus());
    }
}
