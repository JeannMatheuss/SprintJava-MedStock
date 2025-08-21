package br.com.medstock.domain.service;

import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.RegistroDeConsumo;
import br.com.medstock.domain.model.Unidade;
import br.com.medstock.infra.dao.MaterialDAO;
import br.com.medstock.infra.dao.RegistroDeConsumoDAO;
import br.com.medstock.infra.dao.UnidadeDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ConsumoService {

    private Connection conn;
    private MaterialDAO materialDAO;
    private UnidadeDAO unidadeDAO;
    private RegistroDeConsumoDAO registroDAO;

    public ConsumoService(Connection conn) {
        this.conn = conn;
        this.materialDAO = new MaterialDAO(conn);
        this.unidadeDAO = new UnidadeDAO(conn);
        this.registroDAO = new RegistroDeConsumoDAO(conn);
    }

     //Registra o consumo de um material por uma unidade
    public RegistroDeConsumo registrarConsumo(int materialId, int unidadeId, int quantidade) throws SQLException {
        Material material = materialDAO.buscarPorId(materialId);
        Unidade unidade = unidadeDAO.buscarPorId(unidadeId);

        if (material == null) {
            throw new IllegalArgumentException("Material não encontrado.");
        }
        if (unidade == null) {
            throw new IllegalArgumentException("Unidade não encontrada.");
        }
        if (quantidade > material.getQuantidadeDisponivel()) {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque.");
        }

        // Atualiza o estoque
        material.setQuantidadeDisponivel(material.getQuantidadeDisponivel() - quantidade);
        materialDAO.atualizar(material);

        // Cria registro de consumo
        RegistroDeConsumo registro = new RegistroDeConsumo(material, unidade, quantidade);
        registro.setData(LocalDate.now());
        registroDAO.salvar(registro);

        return registro;
    }

    //Lista todos os registros de consumo
    public java.util.List<RegistroDeConsumo> listarTodosRegistros() throws SQLException {
        return registroDAO.listarTodos();
    }
}
