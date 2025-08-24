package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.Funcionario;
import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.RegistroDeConsumo;
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
import javax.persistence.TypedQuery;
import java.time.LocalDate;
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
class JpaRegistroDeConsumoRepositoryTest {

    @Mock
    private EntityManager mockEm;
    @Mock
    private EntityTransaction mockTx;
    @Mock
    private TypedQuery<RegistroDeConsumo> mockQuery;
    @Mock
    private Material mockMaterial;
    @Mock
    private Unidade mockUnidade;
    @Mock
    private Funcionario mockFuncionario;

    @InjectMocks
    private JpaRegistroDeConsumoRepository repository;

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
    @DisplayName("Deve salvar (merge) um registro de consumo e comitar a transação")
    void save_shouldMergeAndCommit() {
        RegistroDeConsumo registro = new RegistroDeConsumo(mockMaterial, mockUnidade, 10, mockFuncionario);
        when(mockEm.merge(any(RegistroDeConsumo.class))).thenReturn(registro);
        RegistroDeConsumo savedRegistro = repository.save(registro);
        assertNotNull(savedRegistro);
        verify(mockTx).begin();
        verify(mockEm).merge(registro);
        verify(mockTx).commit();
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve encontrar um registro por ID quando ele existe")
    void findById_shouldReturnRegistro_whenFound() {
        RegistroDeConsumo registro = new RegistroDeConsumo(mockMaterial, mockUnidade, 10, mockFuncionario);
        when(mockEm.find(RegistroDeConsumo.class, 1)).thenReturn(registro);
        Optional<RegistroDeConsumo> result = repository.findById(1);
        assertTrue(result.isPresent());
        assertEquals(10, result.get().getQuantidade());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao procurar por ID que não existe")
    void findById_shouldReturnEmpty_whenNotFound() {
        when(mockEm.find(RegistroDeConsumo.class, 99)).thenReturn(null);
        Optional<RegistroDeConsumo> result = repository.findById(99);
        assertTrue(result.isEmpty());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar uma lista de todos os registros")
    void findAll_shouldReturnListOfRegistros() {
        List<RegistroDeConsumo> registros = List.of(new RegistroDeConsumo(mockMaterial, mockUnidade, 10, mockFuncionario));
        when(mockEm.createQuery(anyString(), eq(RegistroDeConsumo.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(registros);
        List<RegistroDeConsumo> result = repository.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve retornar uma lista de registros dentro do período especificado")
    void findByPeriodo_shouldReturnFilteredList() {
        LocalDate inicio = LocalDate.now().minusDays(1);
        LocalDate fim = LocalDate.now();
        List<RegistroDeConsumo> registros = List.of(new RegistroDeConsumo(mockMaterial, mockUnidade, 10, mockFuncionario));
        when(mockEm.createQuery(anyString(), eq(RegistroDeConsumo.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(registros);
        List<RegistroDeConsumo> result = repository.findByPeriodo(inicio, fim);
        assertFalse(result.isEmpty());
        verify(mockQuery).setParameter("dataInicio", inicio);
        verify(mockQuery).setParameter("dataFim", fim);
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Deve deletar um registro quando ele existe")
    void deleteById_shouldRemoveRegistro_whenFound() {
        RegistroDeConsumo registro = new RegistroDeConsumo(mockMaterial, mockUnidade, 10, mockFuncionario);
        when(mockEm.find(RegistroDeConsumo.class, 1)).thenReturn(registro);
        repository.deleteById(1);
        verify(mockTx).begin();
        verify(mockEm).remove(registro);
        verify(mockTx).commit();
        verify(mockEm).close();
    }

    @Test
    @DisplayName("Não deve fazer nada ao tentar deletar um registro que não existe")
    void deleteById_shouldDoNothing_whenNotFound() {
        when(mockEm.find(RegistroDeConsumo.class, 99)).thenReturn(null);
        repository.deleteById(99);
        verify(mockEm, never()).remove(any());
        verify(mockTx).commit();
        verify(mockEm).close();
    }
}