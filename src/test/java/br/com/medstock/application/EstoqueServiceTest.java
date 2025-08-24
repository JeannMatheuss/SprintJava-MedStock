package br.com.medstock.application;

import br.com.medstock.domain.exception.EstoqueException;
import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.TipoMaterial;
import br.com.medstock.domain.repository.MaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstoqueServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private EstoqueService estoqueService;

    @Test
    @DisplayName("Deve adicionar estoque com sucesso quando não estiver no máximo")
    void deveAdicionarEstoqueComSucesso() throws Exception {
        Material astraZeneca = criarMaterial(30);
        when(materialRepository.findById(1)).thenReturn(Optional.of(astraZeneca));
        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Material materialAtualizado = estoqueService.adicionarEstoque();

        assertEquals(40, materialAtualizado.getQuantidadeDisponivel());
        verify(materialRepository, times(1)).findById(1);
        verify(materialRepository, times(1)).save(astraZeneca);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar estoque quando já está no máximo")
    void deveLancarExcecaoAoAdicionarNoEstoqueMaximo() throws Exception {
        Material astraZeneca = criarMaterial(50);
        when(materialRepository.findById(1)).thenReturn(Optional.of(astraZeneca));

        EstoqueException exception = assertThrows(EstoqueException.class, () -> {
            estoqueService.adicionarEstoque();
        });

        assertEquals("Estoque máximo de 50 unidades já foi atingido.", exception.getMessage());
        verify(materialRepository, never()).save(any(Material.class));
    }

    @Test
    @DisplayName("Deve remover estoque com sucesso quando não estiver no mínimo")
    void deveRemoverEstoqueComSucesso() throws Exception {
        Material astraZeneca = criarMaterial(20);
        when(materialRepository.findById(1)).thenReturn(Optional.of(astraZeneca));
        when(materialRepository.save(any(Material.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Material materialAtualizado = estoqueService.removerEstoque();

        assertEquals(10, materialAtualizado.getQuantidadeDisponivel());
        verify(materialRepository, times(1)).save(astraZeneca);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover estoque quando já está no mínimo")
    void deveLancarExcecaoAoRemoverDoEstoqueMinimo() throws Exception {
        Material astraZeneca = criarMaterial(0);
        when(materialRepository.findById(1)).thenReturn(Optional.of(astraZeneca));

        assertThrows(EstoqueException.class, () -> {
            estoqueService.removerEstoque();
        });

        verify(materialRepository, never()).save(any(Material.class));
    }

    private Material criarMaterial(int quantidade) throws Exception {
        Material material = new Material();
        setField(material, "id", 1);
        setField(material, "nome", "AstraZeneca");
        setField(material, "tipo", TipoMaterial.REAGENTE);
        setField(material, "quantidadeDisponivel", quantidade);
        return material;
    }

    private void setField(Object targetObject, String fieldName, Object value) throws Exception {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, value);
    }
}