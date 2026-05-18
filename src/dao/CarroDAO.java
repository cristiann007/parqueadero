package dao;

import conexion.DatabaseConnection;
import modelo.Carro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarroDAO {

    public void agregar(Carro c) throws SQLException {
        String sqlInsert = "INSERT INTO carros (placa, marca, modelo, color, id_espacio, fecha_ingreso) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdateEspacio = "UPDATE espacios SET estado=FALSE WHERE id_espacio=?";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

      
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                stmt.setString(1, c.getPlaca());
                stmt.setString(2, c.getMarca());
                stmt.setString(3, c.getModelo());
                stmt.setString(4, c.getColor());
                stmt.setInt(5, c.getIdEspacio());
                stmt.setTimestamp(6, c.getFechaIngreso());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateEspacio)) {
                stmt.setInt(1, c.getIdEspacio());
                stmt.executeUpdate();
            }

            conn.commit(); 
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Carro> listar() throws SQLException {
        List<Carro> lista = new ArrayList<>();
       
        String sql = "SELECT c.*, e.numero_espacio FROM carros c INNER JOIN espacios e ON c.id_espacio = e.id_espacio";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Carro c = new Carro(
                    rs.getInt("id_carro"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("color"),
                    rs.getInt("id_espacio"),
                    rs.getTimestamp("fecha_ingreso")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    public void eliminar(int idCarro, int idEspacio) throws SQLException {
        String sqlDeleteCarro = "DELETE FROM carros WHERE id_carro=?";
        String sqlUpdateEspacio = "UPDATE espacios SET estado=TRUE WHERE id_espacio=?";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateEspacio)) {
                stmt.setInt(1, idEspacio);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteCarro)) {
                stmt.setInt(1, idCarro);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void actualizar(Carro c) throws SQLException {
        String sql = "UPDATE carros SET placa=?, marca=?, modelo=?, color=? WHERE id_carro=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getPlaca());
            stmt.setString(2, c.getMarca());
            stmt.setString(3, c.getModelo());
            stmt.setString(4, c.getColor());
            stmt.setInt(5, c.getIdCarro());
            stmt.executeUpdate();
        }
    }
}
