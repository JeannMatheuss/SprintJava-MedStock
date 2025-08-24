package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.TipoMaterial;
import br.com.medstock.infrastructure.config.JpaUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaMaterialRepositoryTest {

    @Mock
    private EntityManager mockEm;
    @Mock
    private EntityTransaction mockTx;
    @Mock
    private TypedQuery<Material> mockQuery;

    @InjectMocks
    private JpaMaterialRepository repository;

    private MockedStatic<JpaUtil> mockedJpaUtil;

    @BeforeEach
    void setUp() {
        mockedJpaUtil = Mockito.mockStatic(JpaUtil.class);
        mockedJpaUtil.when(JpaUtil::getEntityManager).thenReturn(mockEm);
        lenient().when(mockEm.getTransaction()).thenReturn(mockTx);
    }

    @AfterEach
    void tearDown() {
        mockedJpaUtil.close();
    }

    @Test
    @DisplayName("Deve salvar (merge) um material e comitar a transação")
    void save_shouldMergeAndCommit() throws Exception {
        Material material = criarMaterialParaTeste(null, "Teste", TipoMaterial.REAGENTE, 10);
        when(mockEm.merge(any(Material.class))).thenReturn(material);

        Material savedMaterial = repository.save(material);

        assertNotNull(savedMaterial);
        assertEquals("Teste", savedMaterial.getNome());
        verify(mockTx).begin();
        verify(mockEm).merge(material);
        verify(mockTx).commit();
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve encontrar um material por ID quando ele existe")
    void findById_shouldReturnOptionalWithMaterial_whenFound() throws Exception {
        Material material = criarMaterialParaTeste(1, "AstraZeneca", TipoMaterial.REAGENTE, 30);
        when(mockEm.find(Material.class, 1)).thenReturn(material);

        Optional<Material> result = repository.findById(1);

        assertTrue(result.isPresent());
        assertEquals("AstraZeneca", result.get().getNome());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao procurar por ID que não existe")
    void findById_shouldReturnEmptyOptional_whenNotFound() {
        when(mockEm.find(Material.class, 99)).thenReturn(null);
        Optional<Material> result = repository.findById(99);
        assertTrue(result.isEmpty());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar uma lista de todos os materiais")
    void findAll_shouldReturnListOfMaterials() throws Exception {
        List<Material> materiais = List.of(criarMaterialParaTeste(1, "Material A", TipoMaterial.REAGENTE, 10));
        when(mockEm.createQuery(anyString(), eq(Material.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(materiais);

        List<Material> result = repository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar uma lista de materiais com estoque abaixo do limite")
    void findByEstoqueAbaixoDe_shouldReturnFilteredList() throws Exception {
        int limite = 50;
        List<Material> materiaisFiltrados = List.of(criarMaterialParaTeste(2, "Estoque Baixo", TipoMaterial.DESCARTAVEL, 20));
        when(mockEm.createQuery(anyString(), eq(Material.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(materiaisFiltrados);

        List<Material> result = repository.findByEstoqueAbaixoDe(limite);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(mockQuery).setParameter("limite", limite);
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve deletar um material quando ele existe")
    void deleteById_shouldRemoveMaterial_whenFound() throws Exception {
        Material material = criarMaterialParaTeste(1, "Para Deletar", TipoMaterial.REAGENTE, 10);
        when(mockEm.find(Material.class, 1)).thenReturn(material);

        repository.deleteById(1);

        verify(mockTx).begin();
        verify(mockEm).remove(material);
        verify(mockTx).commit();
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Não deve fazer nada ao tentar deletar um material que não existe")
    void deleteById_shouldDoNothing_whenNotFound() {
        when(mockEm.find(Material.class, 99)).thenReturn(null);
        repository.deleteById(99);
        verify(mockEm, never()).remove(any());
        verify(mockTx).commit();
        verify(mockEm).close();
    }

    private Material criarMaterialParaTeste(Integer id, String nome, TipoMaterial tipo, int quantidade) throws Exception {
        Material material = new Material();
        setField(material, "id", id);
        setField(material, "nome", nome);
        setField(material, "tipo", tipo);
        setField(material, "quantidadeDisponivel", quantidade);
        return material;
    }

    private void setField(Object targetObject, String fieldName, Object value) throws Exception {
        Field field = targetObject.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(targetObject, value);
    }
}