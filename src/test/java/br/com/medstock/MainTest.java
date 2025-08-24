package br.com.medstock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    void testMainNaoLancaExcecao() {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }
}
