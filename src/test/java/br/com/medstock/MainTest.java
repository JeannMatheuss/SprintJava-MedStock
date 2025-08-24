package br.com.medstock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class MainTest {

    @Test
    @DisplayName("O método main deve chamar startApp sem erros")
    void main_shouldCallStartApp() {
        try (MockedStatic<Main> mockedMain = mockStatic(Main.class, CALLS_REAL_METHODS)) {
            mockedMain.when(Main::startApp).then(invocation -> null);

            assertDoesNotThrow(() -> Main.main(null));

            mockedMain.verify(Main::startApp);
        }
    }

    @Test
    @DisplayName("startApp deve chamar showErrorAndExit em caso de falha")
    void startApp_shouldCallShowErrorAndExitOnFailure() {
        try (MockedConstruction<?> mockedRepo = mockConstruction(
                br.com.medstock.infrastructure.persistence.JpaMaterialRepository.class,
                (mock, context) -> { throw new RuntimeException("Falha simulada"); });
             MockedStatic<Main> mockedMain = mockStatic(Main.class, CALLS_REAL_METHODS)) {

            mockedMain.when(() -> Main.showErrorAndExit(any(Exception.class)))
                    .then(invocation -> null);

            Main.startApp();

            mockedMain.verify(() -> Main.showErrorAndExit(any(RuntimeException.class)));
        }
    }
}