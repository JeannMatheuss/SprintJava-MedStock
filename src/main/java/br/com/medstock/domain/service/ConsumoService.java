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
import java.util.*;
import java.util.stream.Collectors;

public class ConsumoService {

    private MaterialDAO materialDAO;
    private UnidadeDAO unidadeDAO;
    private RegistroDeConsumoDAO registroDAO;

    public ConsumoService(Connection conn) {
        this.materialDAO = new MaterialDAO(conn);
        this.unidadeDAO = new UnidadeDAO(conn);
        this.registroDAO = new RegistroDeConsumoDAO(conn);
    }

    // ==================== CRUD ====================

    public RegistroDeConsumo registrarConsumo(int materialId, int unidadeId, int quantidade) throws SQLException {
        Material material = materialDAO.buscarPorId(materialId);
        Unidade unidade = unidadeDAO.buscarPorId(unidadeId);

        if (material == null) throw new IllegalArgumentException("Material não encontrado.");
        if (unidade == null) throw new IllegalArgumentException("Unidade não encontrada.");
        if (quantidade > material.getQuantidadeDisponivel())
            throw new IllegalArgumentException("Quantidade insuficiente em estoque.");

        // Atualiza estoque
        material.setQuantidadeDisponivel(material.getQuantidadeDisponivel() - quantidade);
        materialDAO.atualizar(material);

        // Cria registro de consumo
        RegistroDeConsumo registro = new RegistroDeConsumo(material, unidade, quantidade);
        registro.setData(LocalDate.now());
        registroDAO.salvar(registro);

        return registro;
    }

    public List<RegistroDeConsumo> listarTodosRegistros() throws SQLException {
        return registroDAO.listarTodos();
    }

    // ==================== MÉTODOS importantes ====================

    // 1. Materiais com estoque abaixo do limite
    public List<Material> materiaisComEstoqueBaixo(int limite) throws SQLException {
        List<Material> todos = materialDAO.listarTodos();
        return todos.stream()
                .filter(m -> m.getQuantidadeDisponivel() <= limite)
                .collect(Collectors.toList());
    }

    // 2. Registrar consumo automático (atalho)
    public void registrarConsumoAutomático(int materialId, int unidadeId, int quantidade) throws SQLException {
        registrarConsumo(materialId, unidadeId, quantidade);
        System.out.println("Consumo registrado e estoque atualizado.");
    }

    // 3. Consumo total por material
    public int consumoTotalPorMaterial(int materialId) throws SQLException {
        List<RegistroDeConsumo> registros = registroDAO.listarTodos();
        return registros.stream()
                .filter(r -> r.getMaterial().getId() == materialId)
                .mapToInt(RegistroDeConsumo::getQuantidade)
                .sum();
    }

    // 4. Listar materiais mais usados
    public List<Material> materiaisMaisUsados() throws SQLException {
        List<RegistroDeConsumo> registros = registroDAO.listarTodos();
        Map<Integer, Integer> consumoMap = new HashMap<>();

        for (RegistroDeConsumo r : registros) {
            consumoMap.put(r.getMaterial().getId(),
                    consumoMap.getOrDefault(r.getMaterial().getId(), 0) + r.getQuantidade());
        }

        return consumoMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue() - e1.getValue())
                .map(e -> {
                    try {
                        return materialDAO.buscarPorId(e.getKey());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
