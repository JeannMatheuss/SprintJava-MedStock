package br.com.medstock.infra.bd;

import br.com.medstock.domain.model.RegistroDeConsumo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroDeConsumoDAO {
    private Connection conn;

    public RegistroDeConsumoDAO(Connection conn) {
        this.conn = conn;
    }

    public void salvar(RegistroDeConsumo registro) throws SQLException {
        String sql = "INSERT INTO registro_consumo (material_id, unidade_id, quantidade, data) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, registro.getMaterial().getId());
        stmt.setInt(2, registro.getUnidade().getId());
        stmt.setInt(3, registro.getQuantidade());
        stmt.setDate(4, java.sql.Date.valueOf(registro.getData()));
        stmt.executeUpdate();
    }

    public List<RegistroDeConsumo> listarTodos() throws SQLException {
        List<RegistroDeConsumo> registros = new ArrayList<>();
        String sql = "SELECT * FROM registro_consumo";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            // Aqui você cria o objeto RegistroDeConsumo com Material e Unidade carregados
        }
        return registros;
    }
}
