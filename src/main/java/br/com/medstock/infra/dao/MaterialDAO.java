package br.com.medstock.infra.dao;

import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.TipoMaterial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    private Connection conn;

    public MaterialDAO(Connection conn) {
        this.conn = conn;
    }

    public void salvar(Material material) throws SQLException {
        String sql = "INSERT INTO material (nome, tipo, quantidade_disponivel) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, material.getNome());
        stmt.setString(2, material.getTipo().name());
        stmt.setInt(3, material.getQuantidadeDisponivel());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            material.setId(rs.getInt(1)); // pega o ID gerado pelo banco
        }
    }

    public Material buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM material WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Material(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    TipoMaterial.valueOf(rs.getString("tipo")),
                    rs.getInt("quantidade_disponivel")
            );
        }
        return null;
    }

    public List<Material> listarTodos() throws SQLException {
        List<Material> materiais = new ArrayList<>();
        String sql = "SELECT * FROM material";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            materiais.add(new Material(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    TipoMaterial.valueOf(rs.getString("tipo")),
                    rs.getInt("quantidade_disponivel")
            ));
        }
        return materiais;
    }

    public void atualizar(Material material) throws SQLException {
        String sql = "UPDATE material SET nome=?, tipo=?, quantidade_disponivel=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, material.getNome());
        stmt.setString(2, material.getTipo().name());
        stmt.setInt(3, material.getQuantidadeDisponivel());
        stmt.setInt(4, material.getId());
        stmt.executeUpdate();
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM material WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
