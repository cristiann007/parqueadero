package vista;

import dao.CarroDAO;
import dao.EspacioDAO;
import dao.ParqueaderoDAO;
import modelo.Carro;
import modelo.Espacio;
import modelo.Parqueadero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.util.List;

public class VistaPrincipal extends JFrame {

    // === PALETA DE COLORES AZUL Y NEGRO CON TEXTO NEGRO ===
    private final Color COLOR_FONDO_APP = new Color(240, 242, 245);   // Gris muy claro (Fondo general)
    private final Color COLOR_TARJETA = Color.WHITE;                 // Blanco puro (Paneles/Formularios)
    private final Color COLOR_AZUL_BRILLANTE = new Color(37, 99, 235);// Azul intenso (Acentos)
    private final Color COLOR_VERDE_EXITO = new Color(22, 163, 74);   // Verde oscuro (Entrada)
    private final Color COLOR_ROJO_PELIGRO = new Color(220, 38, 38);  // Rojo oscuro (Salida)
    
    // TEXTOS EN NEGRO
    private final Color COLOR_TEXTO_NEGRO = Color.BLACK;             // Texto principal
    private final Color COLOR_TEXTO_GRIS_OSCURO = new Color(55, 65, 81); // Texto secundario (Labels)
    
    private final Color COLOR_BORDE = new Color(203, 213, 225);       // Bordes sutiles grises

    // === DAOs ===
    private final ParqueaderoDAO pDao = new ParqueaderoDAO();
    private final EspacioDAO eDao = new EspacioDAO();
    private final CarroDAO cDao = new CarroDAO();

    // === MODELOS DE TABLAS ===
    private DefaultTableModel modelCarros;
    private DefaultTableModel modelHistorial;
    private JTable tableCarros;

    public VistaPrincipal() {
        setTitle("Sistema de Gestión de Parqueadero - Pro");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO_APP);

        // Look & Feel Nimbus para estilo moderno
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(COLOR_TEXTO_NEGRO);
        
        tabs.addTab("📊 Dashboard", crearDashboardMejorado());
        tabs.addTab("🏢 Parqueaderos", crearPanelParqueaderosEstilizado());
        tabs.addTab("🅿️ Espacios", crearPanelEspaciosEstilizado());
        tabs.addTab("🚗 Entrada / Salida", crearPanelCarrosEstilizado());
        tabs.addTab("📜 Historial", crearPanelHistorialEstilizado());

        add(tabs);
    }

    // ========================================================================
    // 1. DASHBOARD MEJORADO (TEXTO NEGRO)
    // ========================================================================
    private JPanel crearDashboardMejorado() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(COLOR_FONDO_APP);

        int totalParq = getCount(pDao);
        int totalEsp = getCount(eDao);
        int libres = getLibres(eDao);
        int carros = getCount(cDao);

        mainPanel.add(crearTarjetaDash("Total Parqueaderos", String.valueOf(totalParq), "🏢", COLOR_AZUL_BRILLANTE));
        mainPanel.add(crearTarjetaDash("Total Espacios", String.valueOf(totalEsp), "🅿️", new Color(79, 70, 229)));
        mainPanel.add(crearTarjetaDash("Espacios Libres", String.valueOf(libres), "✅", COLOR_VERDE_EXITO));
        mainPanel.add(crearTarjetaDash("Carros Estacionados", String.valueOf(carros), "🚗", COLOR_ROJO_PELIGRO));

        return mainPanel;
    }

    private JPanel crearTarjetaDash(String titulo, String valor, String icono, Color colorAcento) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAcento, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblIconTitle = new JLabel(icono + " " + titulo);
        lblIconTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblIconTitle.setForeground(COLOR_TEXTO_GRIS_OSCURO); // Texto gris oscuro
        lblIconTitle.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblValor.setForeground(colorAcento);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblIconTitle, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    // ========================================================================
    // 2. PARQUEADEROS ESTILIZADO (TEXTO NEGRO)
    // ========================================================================
    private JPanel crearPanelParqueaderosEstilizado() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(COLOR_FONDO_APP);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBackground(COLOR_TARJETA);
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField tNom = crearInputClaro();
        JTextField tDir = crearInputClaro();
        JTextField tCap = crearInputClaro();
        JTextField tTar = crearInputClaro();

        form.add(crearLabel("Nombre:")); form.add(tNom);
        form.add(crearLabel("Dirección:")); form.add(tDir);
        form.add(crearLabel("Capacidad:")); form.add(tCap);
        form.add(crearLabel("Tarifa ($):")); form.add(tTar);

        JButton btn = new JButton("💾 Guardar Parqueadero");
        estilizarBoton(btn, COLOR_AZUL_BRILLANTE);
        form.add(new JLabel()); form.add(btn);

        p.add(form, BorderLayout.NORTH);

        JTable t = crearTabla(new String[]{"ID", "Nombre", "Dirección", "Capacidad", "Tarifa"});
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        btn.addActionListener(e -> {
            try {
                Parqueadero par = new Parqueadero();
                par.setNombre(tNom.getText());
                par.setDireccion(tDir.getText());
                par.setCapacidad(Integer.parseInt(tCap.getText()));
                par.setTarifaPorHora(Double.parseDouble(tTar.getText()));
                pDao.agregar(par);
                JOptionPane.showMessageDialog(this, "Guardado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        return p;
    }

    // ========================================================================
    // 3. ESPACIOS ESTILIZADO (TEXTO NEGRO)
    // ========================================================================
    private JPanel crearPanelEspaciosEstilizado() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(COLOR_FONDO_APP);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        form.setBackground(COLOR_TARJETA);
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField tId = crearInputClaro(5);
        JTextField tNum = crearInputClaro(10);
        JButton btn = new JButton("➕ Crear Espacio");
        estilizarBoton(btn, COLOR_AZUL_BRILLANTE);

        form.add(crearLabel("ID Parqueadero:")); form.add(tId);
        form.add(crearLabel("Número (Ej: A-01):")); form.add(tNum);
        form.add(btn);

        p.add(form, BorderLayout.NORTH);

        JTable t = crearTabla(new String[]{"ID", "ID Parq", "Número", "Estado"});
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        btn.addActionListener(e -> {
            try {
                Espacio esp = new Espacio();
                esp.setIdParqueadero(Integer.parseInt(tId.getText()));
                esp.setNumeroEspacio(tNum.getText().toUpperCase());
                esp.setEstado(true);
                eDao.agregar(esp);
                JOptionPane.showMessageDialog(this, "Espacio creado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });
        return p;
    }

    // ========================================================================
    // 4. ENTRADA / SALIDA ESTILIZADO (TEXTO NEGRO)
    // ========================================================================
    private JPanel crearPanelCarrosEstilizado() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(COLOR_FONDO_APP);

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBackground(COLOR_TARJETA);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        formPanel.setMaximumSize(new Dimension(1000, 120));

        JTextField txtPlaca = crearInputClaro();
        JTextField txtMarca = crearInputClaro();
        JTextField txtModelo = crearInputClaro();
        JTextField txtColor = crearInputClaro();

        formPanel.add(crearLabel("Placa:")); formPanel.add(txtPlaca);
        formPanel.add(crearLabel("Marca:")); formPanel.add(txtMarca);
        formPanel.add(crearLabel("Modelo:")); formPanel.add(txtModelo);
        formPanel.add(crearLabel("Color:")); formPanel.add(txtColor);

        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(COLOR_FONDO_APP);
        btnPanel.setMaximumSize(new Dimension(1000, 60));

        JButton btnEntrada = new JButton("✅ REGISTRAR ENTRADA");
        estilizarBoton(btnEntrada, COLOR_VERDE_EXITO);
        btnEntrada.setPreferredSize(new Dimension(220, 45));

        JButton btnSalida = new JButton("💸 REGISTRAR SALIDA");
        estilizarBoton(btnSalida, COLOR_ROJO_PELIGRO);
        btnSalida.setPreferredSize(new Dimension(220, 45));

        btnPanel.add(btnEntrada);
        btnPanel.add(btnSalida);
        mainPanel.add(btnPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        modelCarros = new DefaultTableModel(new String[]{"ID", "Placa", "Marca", "Modelo", "Color", "Espacio", "Fecha"}, 0);
        tableCarros = new JTable(modelCarros);
        estilizarTabla(tableCarros);
        JScrollPane scroll = new JScrollPane(tableCarros);
        scroll.setPreferredSize(new Dimension(900, 300));
        mainPanel.add(scroll);

        btnEntrada.addActionListener(e -> {
            try {
                if(txtPlaca.getText().isEmpty()) throw new Exception("Falta Placa");
                Espacio libre = eDao.buscarEspacioLibre(1);
                if(libre == null) { JOptionPane.showMessageDialog(this, "No hay espacios libres", "Lleno", JOptionPane.WARNING_MESSAGE); return; }

                Carro c = new Carro();
                c.setPlaca(txtPlaca.getText().toUpperCase());
                c.setMarca(txtMarca.getText());
                c.setModelo(txtModelo.getText());
                c.setColor(txtColor.getText());
                c.setIdEspacio(libre.getIdEspacio());
                c.setFechaIngreso(new Timestamp(new Date().getTime()));
                
                cDao.agregar(c);
                JOptionPane.showMessageDialog(this, "Ingresado en espacio: " + libre.getNumeroEspacio(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                txtPlaca.setText(""); txtMarca.setText(""); txtModelo.setText(""); txtColor.setText("");
                cargarCarrosSimple();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSalida.addActionListener(e -> {
            int row = tableCarros.getSelectedRow();
            if(row < 0) { JOptionPane.showMessageDialog(this, "Selecciona un carro de la tabla", "Advertencia", JOptionPane.WARNING_MESSAGE); return; }

            try {
                int idCarro = (int) modelCarros.getValueAt(row, 0);
                int idEspacio = (int) modelCarros.getValueAt(row, 5);
                Timestamp fechaIn = (Timestamp) modelCarros.getValueAt(row, 6);
                String placa = (String) modelCarros.getValueAt(row, 1);
                String marca = (String) modelCarros.getValueAt(row, 2);
                String modelo = (String) modelCarros.getValueAt(row, 3);
                String color = (String) modelCarros.getValueAt(row, 4);

                Parqueadero par = pDao.obtenerPorId(1);
                double tarifa = par.getTarifaPorHora();
                
                Timestamp ahora = new Timestamp(new Date().getTime());
                long horas = (ahora.getTime() - fechaIn.getTime()) / (1000 * 60 * 60);
                if(horas < 1) horas = 1;
                double total = horas * tarifa;

                int ok = JOptionPane.showConfirmDialog(this, 
                    "<html><b>Resumen de Salida:</b><br>" +
                    "Placa: " + placa + "<br>" +
                    "Horas: " + horas + "<br>" +
                    "Tarifa: $" + tarifa + "/h<br>" +
                    "<h2 style='color:green'>TOTAL A PAGAR: $" + total + "</h2></html>", 
                    "Confirmar Pago", JOptionPane.YES_NO_OPTION);
                
                if(ok == JOptionPane.YES_OPTION) {
                    guardarHistorialSimple(placa, marca, modelo, color, idEspacio, fechaIn, ahora, horas, total);
                    cDao.eliminar(idCarro, idEspacio);
                    
                    JOptionPane.showMessageDialog(this, "Salida registrada y pago confirmado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarCarrosSimple();
                    cargarHistorialSimple();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cargarCarrosSimple();
        return mainPanel;
    }

    // ========================================================================
    // 5. HISTORIAL ESTILIZADO (TEXTO NEGRO)
    // ========================================================================
    private JPanel crearPanelHistorialEstilizado() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.setBackground(COLOR_FONDO_APP);

        modelHistorial = new DefaultTableModel(new String[]{"Placa", "Marca", "Modelo", "Color", "Espacio", "Entrada", "Salida", "Horas", "Total"}, 0);
        JTable t = new JTable(modelHistorial);
        estilizarTabla(t);
        
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        cargarHistorialSimple();
        return p;
    }

    // ========================================================================
    // UTILIDADES DE ESTILO (TEXTO NEGRO)
    // ========================================================================

    // Input con fondo BLANCO y texto NEGRO para legibilidad
    private JTextField crearInputClaro() {
        JTextField f = new JTextField();
        f.setBackground(Color.WHITE);
        f.setForeground(COLOR_TEXTO_NEGRO);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return f;
    }

    private JTextField crearInputClaro(int cols) {
        JTextField f = crearInputClaro();
        f.setColumns(cols);
        return f;
    }

    // Labels en Negro o Gris Oscuro
    private JLabel crearLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(COLOR_TEXTO_GRIS_OSCURO);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private void estilizarBoton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE); // Texto del botón sigue siendo blanco para contraste
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JTable crearTabla(String[] cols) {
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        JTable t = new JTable(m);
        estilizarTabla(t);
        return t;
    }

    // Tabla con encabezados oscuros pero texto de celdas NEGRO
    private void estilizarTabla(JTable t) {
        t.setBackground(Color.WHITE);
        t.setForeground(COLOR_TEXTO_NEGRO); // Texto de las celdas en NEGRO
        t.setSelectionBackground(COLOR_AZUL_BRILLANTE);
        t.setSelectionForeground(Color.WHITE);
        t.setGridColor(COLOR_BORDE);
        
        JTableHeader h = t.getTableHeader();
        h.setBackground(new Color(30, 41, 59)); // Encabezado oscuro
        h.setForeground(Color.WHITE);           // Texto encabezado blanco
        h.setFont(new Font("Segoe UI", Font.BOLD, 12));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE));
    }

    private int getCount(Object dao) {
        try {
            if(dao instanceof ParqueaderoDAO) return ((ParqueaderoDAO)dao).listar().size();
            if(dao instanceof EspacioDAO) return ((EspacioDAO)dao).listar().size();
            if(dao instanceof CarroDAO) return ((CarroDAO)dao).listar().size();
        } catch (SQLException e) { return 0; }
        return 0;
    }
    
    private int getLibres(EspacioDAO dao) {
        try {
            return (int) dao.listar().stream().filter(Espacio::isEstado).count();
        } catch (SQLException e) { return 0; }
    }

    private void cargarCarrosSimple() {
        modelCarros.setRowCount(0);
        try {
            for(Carro c : cDao.listar()) {
                modelCarros.addRow(new Object[]{c.getIdCarro(), c.getPlaca(), c.getMarca(), c.getModelo(), c.getColor(), c.getIdEspacio(), c.getFechaIngreso()});
            }
        } catch(SQLException e) {}
    }

    private void cargarHistorialSimple() {
        if(modelHistorial == null) return;
        modelHistorial.setRowCount(0);
        try(Connection conn = conexion.DatabaseConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM historial_salidas ORDER BY fecha_registro DESC")) {
            
            while(rs.next()) {
                modelHistorial.addRow(new Object[]{
                    rs.getString("placa"), rs.getString("marca"), rs.getString("modelo"), rs.getString("color"),
                    rs.getInt("espacio_usado"), rs.getTimestamp("fecha_ingreso"), rs.getTimestamp("fecha_salida"),
                    rs.getLong("horas_estadia"), "$" + String.format("%.2f", rs.getDouble("total_pagado"))
                });
            }
        } catch(SQLException e) { e.printStackTrace(); }
    }

    private void guardarHistorialSimple(String placa, String marca, String modelo, String color, int espacio, Timestamp in, Timestamp out, long horas, double total) throws SQLException {
        String sql = "INSERT INTO historial_salidas (placa, marca, modelo, color, espacio_usado, fecha_ingreso, fecha_salida, horas_estadia, total_pagado) VALUES (?,?,?,?,?,?,?,?,?)";
        try(Connection conn = conexion.DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, placa); ps.setString(2, marca); ps.setString(3, modelo); ps.setString(4, color);
            ps.setInt(5, espacio); ps.setTimestamp(6, in); ps.setTimestamp(7, out);
            ps.setLong(8, horas); ps.setDouble(9, total);
            ps.executeUpdate();
        }
    }
}    

