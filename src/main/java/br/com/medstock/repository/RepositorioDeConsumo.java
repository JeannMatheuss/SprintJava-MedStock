package br.com.medstock.repository;

import br.com.medstock.model.RegistroDeConsumo;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeConsumo {
    private List<RegistroDeConsumo> registros = new ArrayList<>();

    public void salvar(RegistroDeConsumo registro) {
        registros.add(registro);
    }

    public List<RegistroDeConsumo> listar() {
        return registros;
    }
}
