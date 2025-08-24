package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.Funcionario;
import br.com.medstock.domain.model.ResponsavelAlmoxarifado;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaFuncionarioRepositoryTest {

    @Mock
    private EntityManager mockEm;
    @Mock
    private EntityTransaction mockTx;
    @Mock
    private TypedQuery<Funcionario> mockQuery;

    @InjectMocks
    private JpaFuncionarioRepository repository;

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
    @DisplayName("Deve salvar (merge) um funcionário e comitar a transação")
    void save_shouldMergeAndCommit() {
        Funcionario funcionario = new ResponsavelAlmoxarifado("João", "123");
        when(mockEm.merge(any(Funcionario.class))).thenReturn(funcionario);

        Funcionario savedFuncionario = repository.save(funcionario);

        assertNotNull(savedFuncionario);
        verify(mockTx, times(1)).begin();
        verify(mockEm, times(1)).merge(funcionario);
        verify(mockTx, times(1)).commit();
        verify(mockEm, times(1)).close();
    }

    @Test
    @DisplayName("Deve encontrar um funcionário por ID quando ele existe")
    void findById_shouldReturnOptionalWithFuncionario_whenFound() {
        Funcionario funcionario = new ResponsavelAlmoxarifado("Ana", "456");
        when(mockEm.find(Funcionario.class, 1)).thenReturn(funcionario);

        Optional<Funcionario> result = repository.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Ana", result.get().getNome());
        verify(mockEm, times(1)).close();
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao procurar por ID que não existe")
    void findById_shouldReturnEmptyOptional_whenNotFound() {
        when(mockEm.find(Funcionario.class, 99)).thenReturn(null);

        Optional<Funcionario> result = repository.findById(99);

        assertTrue(result.isEmpty());
        verify(mockEm, times(1)).close();
    }

    @Test
    @DisplayName("Deve encontrar um funcionário por matrícula quando ele existe")
    void findByMatricula_shouldReturnFuncionario_whenFound() {
        Funcionario funcionario = new ResponsavelAlmoxarifado("Carlos", "789");
        when(mockEm.createQuery(anyString(), eq(Funcionario.class))).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(funcionario);

        Optional<Funcionario> result = repository.findByMatricula("789");

        assertTrue(result.isPresent());
        assertEquals("789", result.get().getMatricula());
        verify(mockQuery).setParameter("matricula", "789");
        verify(mockEm, times(1)).close();
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao procurar por matrícula que não existe")
    void findByMatricula_shouldReturnEmpty_whenNotFound() {
        when(mockEm.createQuery(anyString(), eq(Funcionario.class))).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenThrow(new NoResultException());

        Optional<Funcionario> result = repository.findByMatricula("not-found");

        assertTrue(result.isEmpty());
        verify(mockEm, times(1)).close();
    }

    @Test
    @DisplayName("Deve retornar uma lista de todos os funcionários")
    void findAll_shouldReturnListOfFuncionarios() {
        List<Funcionario> funcionarios = List.of(new ResponsavelAlmoxarifado("Maria", "111"));
        when(mockEm.createQuery(anyString(), eq(Funcionario.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(funcionarios);

        List<Funcionario> result = repository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(mockEm, times(1)).close();
    }

    @Test
    @DisplayName("Deve deletar um funcionário quando ele existe")
    void deleteById_shouldRemoveFuncionario_whenFound() {
        Funcionario funcionario = new ResponsavelAlmoxarifado("Pedro", "222");
        when(mockEm.find(Funcionario.class, 1)).thenReturn(funcionario);

        repository.deleteById(1);

        verify(mockTx, times(1)).begin();
        verify(mockEm, times(1)).remove(funcionario);
        verify(mockTx, times(1)).commit();
        verify(mockEm, times(1)).close();
    }

    @Test
    @DisplayName("Não deve fazer nada ao tentar deletar um funcionário que não existe")
    void deleteById_shouldDoNothing_whenNotFound() {
        when(mockEm.find(Funcionario.class, 99)).thenReturn(null);

        repository.deleteById(99);

        verify(mockEm, never()).remove(any());
        verify(mockTx, times(1)).commit();
        verify(mockEm, times(1)).close();
    }
}