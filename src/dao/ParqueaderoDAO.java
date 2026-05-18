package dao;

import conexion.DatabaseConnection;
import modelo.Parqueadero;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParqueaderoDAO {
    public void agregar(Parqueadero p) throws SQLException {
        String sql = "INSERT INTO parqueaderos (nombre, direccion, capacidad, tarifa_por_hora) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getDireccion());
            stmt.setInt(3, p.getCapacidad());
            stmt.setDouble(4, p.getTarifaPorHora());
            stmt.executeUpdate();
        }
    }

    public List<Parqueadero> listar() throws SQLException {
        List<Parqueadero> lista = new ArrayList<>();
        String sql = "SELECT * FROM parqueaderos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Parqueadero p = new Parqueadero(
                    rs.getInt("id_parqueadero"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getInt("capacidad"),
                    rs.getDouble("tarifa_por_hora")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public void actualizar(Parqueadero p) throws SQLException {
        String sql = "UPDATE parqueaderos SET nombre=?, direccion=?, capacidad=?, tarifa_por_hora=? WHERE id_parqueadero=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNombre());
            stmt.setString(2, p.getDireccion());
            stmt.setInt(3, p.getCapacidad());
            stmt.setDouble(4, p.getTarifaPorHora());
            stmt.setInt(5, p.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM parqueaderos WHERE id_parqueadero=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // === MÉTODO NUEVO: OBTENER PARQUEADERO POR ID (NECESARIO PARA CALCULAR TARIFA) ===
    public Parqueadero obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM parqueaderos WHERE id_parqueadero=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Parqueadero(
                    rs.getInt("id_parqueadero"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getInt("capacidad"),
                    rs.getDouble("tarifa_por_hora")
                );
            }
        }
        return null;
    }
}