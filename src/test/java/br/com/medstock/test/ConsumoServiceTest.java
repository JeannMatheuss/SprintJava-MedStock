package br.com.medstock.test;

import br.com.medstock.model.*;
import br.com.medstock.service.ConsumoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConsumoServiceTest {

    private Material material;
    private Unidade unidade;
    private ConsumoService service;

    @BeforeEach
    void setup() {
        material = new Material("Reagente A", TipoMaterial.REAGENTE, 100);
        unidade = new Unidade("Unidade Central", "Bloco A");
        service = new ConsumoService();
    }

    @Test
    void deveRegistrarConsumoValido() {
        RegistroDeConsumo registro = service.registrarConsumo(material, unidade, 30);

        assertEquals(70, material.getQuantidadeDisponivel());
        assertEquals(30, registro.getQuantidade());
        assertEquals(material, registro.getMaterial());
        assertEquals(unidade, registro.getUnidade());
        assertNotNull(registro.getData());
    }

    @Test
    void deveLancarErroQuandoConsumoExcedeEstoque() {
        Exception excecao = assertThrows(IllegalArgumentException.class, () -> {
            service.registrarConsumo(material, unidade, 150);
        });

        assertEquals("Quantidade insuficiente em estoque.", excecao.getMessage());
    }

    @Test
    void naoDeveModificarEstoqueSeErroOcorrer() {
        try {
            service.registrarConsumo(material, unidade, 150);
        } catch (IllegalArgumentException e) {
            // Ignorar
        }

        assertEquals(100, material.getQuantidadeDisponivel());
    }

    @Test
    void devePermitirRegistroComEstoqueExato() {
        RegistroDeConsumo registro = service.registrarConsumo(material, unidade, 100);

        assertEquals(0, material.getQuantidadeDisponivel());
    }
}
