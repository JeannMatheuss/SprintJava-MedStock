package br.com.medstock.infrastructure.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaUtilTest {

    @Mock
    private EntityManagerFactory mockFactory;
    @Mock
    private EntityManager mockEm;
    @Mock
    private Function<String, String> mockEnvProvider;

    private MockedStatic<Persistence> mockedPersistence;

    @BeforeEach
    void setUp() {
        mockedPersistence = Mockito.mockStatic(Persistence.class);
        when(Persistence.createEntityManagerFactory(anyString(), any())).thenReturn(mockFactory);

        JpaUtil.setEnvProvider(mockEnvProvider);
        when(mockEnvProvider.apply(anyString())).thenReturn("test_value");

        when(mockFactory.createEntityManager()).thenReturn(mockEm);
    }

    @AfterEach
    void tearDown() {
        mockedPersistence.close();
        JpaUtil.setEnvProvider(System::getenv);
        JpaUtil.shutdown();
        try {
            java.lang.reflect.Field factoryField = JpaUtil.class.getDeclaredField("FACTORY");
            factoryField.setAccessible(true);
            factoryField.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Deve retornar um EntityManager com sucesso")
    void getEntityManager_shouldReturnEntityManager() {
        EntityManager em = JpaUtil.getEntityManager();
        assertNotNull(em);
        verify(mockFactory).createEntityManager();
    }

    @Test
    @DisplayName("Deve chamar o método close da fábrica durante o shutdown")
    void shutdown_shouldCloseFactory() {
        JpaUtil.getEntityManager();
        when(mockFactory.isOpen()).thenReturn(true);
        JpaUtil.shutdown();
        verify(mockFactory).close();
    }
}