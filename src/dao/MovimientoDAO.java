package dao;

import conexion.DatabaseConnection;
import modelo.Movimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    public void registrarEntrada(Movimiento m) throws SQLException {
        String sql = "INSERT INTO movimientos (id_carro, id_espacio, fecha_ingreso, estado) VALUES (?, ?, ?, 'ACTIVO')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getIdCarro());
            stmt.setInt(2, m.getIdEspacio());
            stmt.setTimestamp(3, m.getFechaIngreso());
            stmt.executeUpdate();
        }
    }

    public void registrarSalida(int idMovimiento, Timestamp fechaSalida, double totalPagar) throws SQLException {
        String sql = "UPDATE movimientos SET fecha_salida=?, total_pagar=?, estado='FINALIZADO' WHERE id_movimiento=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, fechaSalida);
            stmt.setDouble(2, totalPagar);
            stmt.setInt(3, idMovimiento);
            stmt.executeUpdate();
        }
    }

    public List<Movimiento> listarHistorial() throws SQLException {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos ORDER BY fecha_ingreso DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movimiento m = new Movimiento(
                    rs.getInt("id_movimiento"),
                    rs.getInt("id_carro"),
                    rs.getInt("id_espacio"),
                    rs.getTimestamp("fecha_ingreso"),
                    rs.getTimestamp("fecha_salida"),
                    rs.getDouble("total_pagar"),
                    rs.getString("estado")
                );
                lista.add(m);
            }
        }
        return lista;
    }
    
    public Movimiento obtenerMovimientoActivo(int idCarro) throws SQLException {
        String sql = "SELECT * FROM movimientos WHERE id_carro=? AND estado='ACTIVO'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Movimiento(
                    rs.getInt("id_movimiento"),
                    rs.getInt("id_carro"),
                    rs.getInt("id_espacio"),
                    rs.getTimestamp("fecha_ingreso"),
                    rs.getTimestamp("fecha_salida"),
                    rs.getDouble("total_pagar"),
                    rs.getString("estado")
                );
            }
        }
        return null;
    }
}