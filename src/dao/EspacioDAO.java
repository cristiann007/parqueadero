package dao;

import conexion.DatabaseConnection;
import modelo.Espacio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspacioDAO {

    public void agregar(Espacio e) throws SQLException {
        String sql = "INSERT INTO espacios (id_parqueadero, numero_espacio, estado) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdParqueadero());
            stmt.setString(2, e.getNumeroEspacio());
            stmt.setBoolean(3, e.isEstado());
            stmt.executeUpdate();
        }
    }

   
    public List<Espacio> listar() throws SQLException {
        List<Espacio> lista = new ArrayList<>();
        String sql = "SELECT * FROM espacios";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Espacio e = new Espacio(
                    rs.getInt("id_espacio"),
                    rs.getInt("id_parqueadero"),
                    rs.getString("numero_espacio"),
                    rs.getBoolean("estado")
                );
                lista.add(e);
            }
        }
        return lista;
    }

    public void actualizar(Espacio e) throws SQLException {
        String sql = "UPDATE espacios SET id_parqueadero=?, numero_espacio=?, estado=? WHERE id_espacio=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdParqueadero());
            stmt.setString(2, e.getNumeroEspacio());
            stmt.setBoolean(3, e.isEstado());
            stmt.setInt(4, e.getIdEspacio());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM espacios WHERE id_espacio=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

 
    public Espacio buscarEspacioLibre(int idParqueadero) throws SQLException {
        String sql = "SELECT * FROM espacios WHERE id_parqueadero=? AND estado=TRUE LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idParqueadero);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Espacio(
                    rs.getInt("id_espacio"),
                    rs.getInt("id_parqueadero"),
                    rs.getString("numero_espacio"),
                    rs.getBoolean("estado")
                );
            }
        }
        return null; 
    }
    
    public Espacio obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM espacios WHERE id_espacio=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Espacio(
                    rs.getInt("id_espacio"),
                    rs.getInt("id_parqueadero"),
                    rs.getString("numero_espacio"),
                    rs.getBoolean("estado")
                );
            }
        }
        return null;
    }
}