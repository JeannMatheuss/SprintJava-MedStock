package br.com.medstock.infra.dao;

import br.com.medstock.domain.model.RegistroDeConsumo;
import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.Unidade;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistroDeConsumoDAO {
    private Connection conn;

    public RegistroDeConsumoDAO(Connection conn) {
        this.conn = conn;
    }

    public void salvar(RegistroDeConsumo registro) throws SQLException {
        String sql = "INSERT INTO registro_consumo (material_id, unidade_id, quantidade, data) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, registro.getMaterial().getId());
        stmt.setInt(2, registro.getUnidade().getId());
        stmt.setInt(3, registro.getQuantidade());
        stmt.setDate(4, Date.valueOf(registro.getData()));
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            registro.setId(rs.getInt(1)); // adiciona id no registro
        }
    }

    public RegistroDeConsumo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM registro_consumo WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            MaterialDAO materialDAO = new MaterialDAO(conn);
            UnidadeDAO unidadeDAO = new UnidadeDAO(conn);

            Material material = materialDAO.buscarPorId(rs.getInt("material_id"));
            Unidade unidade = unidadeDAO.buscarPorId(rs.getInt("unidade_id"));
            int quantidade = rs.getInt("quantidade");
            LocalDate data = rs.getDate("data").toLocalDate();

            RegistroDeConsumo registro = new RegistroDeConsumo(material, unidade, quantidade);
            registro.setData(data);
            registro.setId(id);
            return registro;
        }
        return null;
    }

    public List<RegistroDeConsumo> listarTodos() throws SQLException {
        List<RegistroDeConsumo> registros = new ArrayList<>();
        String sql = "SELECT * FROM registro_consumo";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        MaterialDAO materialDAO = new MaterialDAO(conn);
        UnidadeDAO unidadeDAO = new UnidadeDAO(conn);

        while(rs.next()) {
            int id = rs.getInt("id");
            Material material = materialDAO.buscarPorId(rs.getInt("material_id"));
            Unidade unidade = unidadeDAO.buscarPorId(rs.getInt("unidade_id"));
            int quantidade = rs.getInt("quantidade");
            LocalDate data = rs.getDate("data").toLocalDate();

            RegistroDeConsumo registro = new RegistroDeConsumo(material, unidade, quantidade);
            registro.setData(data);
            registro.setId(id);
            registros.add(registro);
        }
        return registros;
    }

    public void atualizar(RegistroDeConsumo registro) throws SQLException {
        String sql = "UPDATE registro_consumo SET material_id=?, unidade_id=?, quantidade=?, data=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, registro.getMaterial().getId());
        stmt.setInt(2, registro.getUnidade().getId());
        stmt.setInt(3, registro.getQuantidade());
        stmt.setDate(4, Date.valueOf(registro.getData()));
        stmt.setInt(5, registro.getId());
        stmt.executeUpdate();
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM registro_consumo WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
