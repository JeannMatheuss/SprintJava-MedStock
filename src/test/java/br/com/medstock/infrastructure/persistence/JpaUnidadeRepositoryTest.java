package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.Unidade;
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
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
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
class JpaUnidadeRepositoryTest {

    @Mock
    private EntityManager mockEm;
    @Mock
    private EntityTransaction mockTx;
    @Mock
    private TypedQuery<Unidade> mockQuery;

    @InjectMocks
    private JpaUnidadeRepository repository;

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
    @DisplayName("Deve salvar (merge) uma unidade e comitar a transação")
    void save_shouldMergeAndCommit() {
        Unidade unidade = new Unidade("Laboratório A", "Bloco 1");
        when(mockEm.merge(any(Unidade.class))).thenReturn(unidade);
        Unidade savedUnidade = repository.save(unidade);
        assertNotNull(savedUnidade);
        verify(mockTx).begin();
        verify(mockEm).merge(unidade);
        verify(mockTx).commit();
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve encontrar uma unidade por ID quando ela existe")
    void findById_shouldReturnUnidade_whenFound() {
        Unidade unidade = new Unidade("Laboratório A", "Bloco 1");
        when(mockEm.find(Unidade.class, 1)).thenReturn(unidade);
        Optional<Unidade> result = repository.findById(1);
        assertTrue(result.isPresent());
        assertEquals("Laboratório A", result.get().getNome());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao procurar por ID que não existe")
    void findById_shouldReturnEmpty_whenNotFound() {
        when(mockEm.find(Unidade.class, 99)).thenReturn(null);
        Optional<Unidade> result = repository.findById(99);
        assertTrue(result.isEmpty());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve encontrar uma unidade por nome quando ela existe")
    void findByNome_shouldReturnUnidade_whenFound() {
        Unidade unidade = new Unidade("Laboratório A", "Bloco 1");
        when(mockEm.createQuery(anyString(), eq(Unidade.class))).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(unidade);
        Optional<Unidade> result = repository.findByNome("Laboratório A");
        assertTrue(result.isPresent());
        assertEquals("Laboratório A", result.get().getNome());
        verify(mockQuery).setParameter("nome", "Laboratório A");
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao procurar por nome que não existe")
    void findByNome_shouldReturnEmpty_whenNotFound() {
        when(mockEm.createQuery(anyString(), eq(Unidade.class))).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenThrow(new NoResultException());
        Optional<Unidade> result = repository.findByNome("not-found");
        assertTrue(result.isEmpty());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar uma lista de todas as unidades")
    void findAll_shouldReturnListOfUnidades() {
        List<Unidade> unidades = List.of(new Unidade("Laboratório A", "Bloco 1"));
        when(mockEm.createQuery(anyString(), eq(Unidade.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(unidades);
        List<Unidade> result = repository.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve deletar uma unidade quando ela existe")
    void deleteById_shouldRemoveUnidade_whenFound() {
        Unidade unidade = new Unidade("Para Deletar", "Bloco X");
        when(mockEm.find(Unidade.class, 1)).thenReturn(unidade);
        repository.deleteById(1);
        verify(mockTx).begin();
        verify(mockEm).remove(unidade);
        verify(mockTx).commit();
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Não deve fazer nada ao tentar deletar uma unidade que não existe")
    void deleteById_shouldDoNothing_whenNotFound() {
        when(mockEm.find(Unidade.class, 99)).thenReturn(null);
        repository.deleteById(99);
        verify(mockEm, never()).remove(any());
        verify(mockTx).commit();
        verify(mockEm).close();
    }
}