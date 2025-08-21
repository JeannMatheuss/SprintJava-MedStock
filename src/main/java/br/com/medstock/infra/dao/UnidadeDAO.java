package br.com.medstock.infra.dao;

import br.com.medstock.domain.model.Unidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnidadeDAO {
    private Connection conn;

    public UnidadeDAO(Connection conn) {
        this.conn = conn;
    }

    public void salvar(Unidade unidade) throws SQLException {
        String sql = "INSERT INTO unidade (nome, localizacao) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, unidade.getNome());
        stmt.setString(2, unidade.getLocalizacao());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            unidade.setId(rs.getInt(1));
        }
    }

    public Unidade buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM unidade WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Unidade(rs.getInt("id"), rs.getString("nome"), rs.getString("localizacao"));
        }
        return null;
    }

    public List<Unidade> listarTodos() throws SQLException {
        List<Unidade> unidades = new ArrayList<>();
        String sql = "SELECT * FROM unidade";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            unidades.add(new Unidade(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("localizacao")
            ));
        }
        return unidades;
    }

    public void atualizar(Unidade unidade) throws SQLException {
        String sql = "UPDATE unidade SET nome=?, localizacao=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, unidade.getNome());
        stmt.setString(2, unidade.getLocalizacao());
        stmt.setInt(3, unidade.getId());
        stmt.executeUpdate();
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM unidade WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
