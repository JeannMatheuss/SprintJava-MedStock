package br.com.medstock.domain.model;

import java.awt.Color;

public enum NivelEstoqueStatus {

    OK("OK", new Color(0, 153, 76)),
    PREOCUPANTE("Preocupante", new Color(255, 193, 7)),
    CRITICA("Crítica", new Color(220, 53, 69)),
    EXTREMA("Extrema", Color.BLACK);

    private final String texto;
    private final Color cor;

    NivelEstoqueStatus(String texto, Color cor) {
        this.texto = texto;
        this.cor = cor;
    }

    public String getTexto() {
        return texto;
    }



    public Color getCor() {
        return cor;
    }

    public static NivelEstoqueStatus fromQuantidade(int quantidade) {
        if (quantidade > 20) {
            return OK;
        } else if (quantidade > 10) {
            return PREOCUPANTE;
        } else if (quantidade > 0) {
            return CRITICA;
        } else {
            return EXTREMA;
        }
    }
}