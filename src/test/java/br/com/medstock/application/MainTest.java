package br.com.medstock.application;

import br.com.medstock.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    void testMainNaoLancaExcecao() {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }
}
