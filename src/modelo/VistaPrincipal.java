package modelo;


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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class VistaPrincipal extends JFrame {

    // === PALETA DE COLORES AZUL Y NEGRO PROFESIONAL ===
    private final Color COLOR_FONDO_GENERAL = new Color(18, 24, 38);       // Negro azulado oscuro
    private final Color COLOR_PRIMARIO = new Color(30, 64, 175);           // Azul intenso
    private final Color COLOR_SECUNDARIO = new Color(15, 23, 42);          // Negro casi puro
    private final Color COLOR_EXITO = new Color(34, 197, 94);              // Verde brillante
    private final Color COLOR_PELIGRO = new Color(239, 68, 68);            // Rojo vibrante
    private final Color COLOR_TEXTO_CLARO = new Color(240, 240, 240);      // Blanco suave
    private final Color COLOR_BORDE_SUAVE = new Color(55, 65, 81);         // Gris azulado para bordes
    private final Color COLOR_CAMPO_INPUT = new Color(30, 41, 59);         // Fondo oscuro para inputs
    private final Color COLOR_TABLA_HEADER_BG = new Color(30, 41, 59);     // Fondo oscuro para encabezados
    private final Color COLOR_TABLA_HEADER_TEXT = Color.WHITE;             // Texto blanco para encabezados
    private final Color COLOR_DASHBOARD_CARD = new Color(30, 41, 59);      // Tarjetas del dashboard

    // === DAOs ===
    private final ParqueaderoDAO pDao = new ParqueaderoDAO();
    private final EspacioDAO eDao = new EspacioDAO();
    private final CarroDAO cDao = new CarroDAO();

    // === MODELOS DE TABLAS GLOBALES ===
    private DefaultTableModel modelCarros;
    private DefaultTableModel modelEspacios;
    private JTable tableCarros;

    // === VARIABLES PARA EL DASHBOARD (Para actualizar fácilmente) ===
    private JLabel lblTotalParqueaderos;
    private JLabel lblTotalEspacios;
    private JLabel lblEspaciosLibres;
    private JLabel lblCarrosEstacionados;

    public VistaPrincipal() {
        setTitle("🚗 Sistema de Gestión de Parqueadero - Java JDBC");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO_GENERAL);

        // Usar Nimbus Look & Feel si está disponible
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Ignorar si falla
        }

        // === PESTAÑAS ESTILIZADAS EN AZUL Y NEGRO ===
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(COLOR_SECUNDARIO);
        tabbedPane.setForeground(COLOR_TEXTO_CLARO);

        // Agregar las 5 pestañas (incluyendo Dashboard)
        tabbedPane.addTab("📊 Dashboard", crearPanelDashboard());
        tabbedPane.addTab("🏢 Parqueaderos", crearPanelParqueaderos());
        tabbedPane.addTab("️ Espacios", crearPanelEspacios());
        tabbedPane.addTab("🚘 Entrada / Salida", crearPanelCarros());
        tabbedPane.addTab("📜 Historial", crearPanelHistorial());

        // Forzar colores de pestañas activas/inactivas
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                if (i == selectedIndex) {
                    tabbedPane.setBackgroundAt(i, COLOR_PRIMARIO);
                    tabbedPane.setForegroundAt(i, Color.WHITE);
                } else {
                    tabbedPane.setBackgroundAt(i, COLOR_SECUNDARIO);
                    tabbedPane.setForegroundAt(i, COLOR_TEXTO_CLARO);
                }
            }
        });

        add(tabbedPane);
    }

    // ========================================================================
    // PESTAÑA 0: DASHBOARD (RESUMEN DE DATOS) - CORREGIDO
    // ========================================================================
    private JPanel crearPanelDashboard() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(COLOR_FONDO_GENERAL);

        // Crear las etiquetas de valor que actualizaremos después
        lblTotalParqueaderos = new JLabel("0");
        lblTotalParqueaderos.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotalParqueaderos.setForeground(COLOR_PRIMARIO);
        lblTotalParqueaderos.setHorizontalAlignment(SwingConstants.CENTER);

        lblTotalEspacios = new JLabel("0");
        lblTotalEspacios.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotalEspacios.setForeground(COLOR_EXITO);
        lblTotalEspacios.setHorizontalAlignment(SwingConstants.CENTER);

        lblEspaciosLibres = new JLabel("0");
        lblEspaciosLibres.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblEspaciosLibres.setForeground(new Color(59, 130, 246)); // Azul claro
        lblEspaciosLibres.setHorizontalAlignment(SwingConstants.CENTER);

        lblCarrosEstacionados = new JLabel("0");
        lblCarrosEstacionados.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblCarrosEstacionados.setForeground(COLOR_PELIGRO);
        lblCarrosEstacionados.setHorizontalAlignment(SwingConstants.CENTER);

        // Crear las tarjetas usando los labels creados arriba
        JPanel card1 = crearTarjetaDashboardSimple("🏢 Total Parqueaderos", lblTotalParqueaderos, COLOR_PRIMARIO);
        JPanel card2 = crearTarjetaDashboardSimple("🅿️ Total Espacios", lblTotalEspacios, COLOR_EXITO);
        JPanel card3 = crearTarjetaDashboardSimple("✅ Espacios Libres", lblEspaciosLibres, new Color(59, 130, 246));
        JPanel card4 = crearTarjetaDashboardSimple("🚗 Carros Estacionados", lblCarrosEstacionados, COLOR_PELIGRO);

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);
        panel.add(card4);

        // Cargar datos iniciales
        cargarDatosDashboard();

        return panel;
    }

    // Método auxiliar simplificado para crear tarjetas
    private JPanel crearTarjetaDashboardSimple(String titulo, JLabel lblValor, Color colorAcento) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_DASHBOARD_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAcento, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_TEXTO_CLARO);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);

        return card;
    }

    // Método para cargar/actualizar los datos del dashboard
    private void cargarDatosDashboard() {
        try {
            int totalParqueaderos = pDao.listar().size();
            int totalEspacios = eDao.listar().size();
            int espaciosLibres = (int) eDao.listar().stream().filter(Espacio::isEstado).count();
            int carrosEstacionados = cDao.listar().size();

            lblTotalParqueaderos.setText(String.valueOf(totalParqueaderos));
            lblTotalEspacios.setText(String.valueOf(totalEspacios));
            lblEspaciosLibres.setText(String.valueOf(espaciosLibres));
            lblCarrosEstacionados.setText(String.valueOf(carrosEstacionados));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar dashboard: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // ========================================================================
    // PESTAÑA 1: GESTIÓN DE PARQUEADEROS
    // ========================================================================
    private JPanel crearPanelParqueaderos() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_FONDO_GENERAL);

        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.setBackground(COLOR_DASHBOARD_CARD);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel gridForm = new JPanel(new GridLayout(4, 2, 15, 15));
        gridForm.setBackground(COLOR_DASHBOARD_CARD);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        JLabel lblNombre = new JLabel("Nombre del Parqueadero:");
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(COLOR_TEXTO_CLARO);

        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(labelFont);
        lblDireccion.setForeground(COLOR_TEXTO_CLARO);

        JLabel lblCapacidad = new JLabel("Capacidad Total:");
        lblCapacidad.setFont(labelFont);
        lblCapacidad.setForeground(COLOR_TEXTO_CLARO);

        JLabel lblTarifa = new JLabel("Tarifa por Hora ($):");
        lblTarifa.setFont(labelFont);
        lblTarifa.setForeground(COLOR_TEXTO_CLARO);

        JTextField txtNombre = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtCapacidad = new JTextField();
        JTextField txtTarifa = new JTextField();

        estilizarCampoTextoOscuro(txtNombre);
        estilizarCampoTextoOscuro(txtDireccion);
        estilizarCampoTextoOscuro(txtCapacidad);
        estilizarCampoTextoOscuro(txtTarifa);

        gridForm.add(lblNombre); gridForm.add(txtNombre);
        gridForm.add(lblDireccion); gridForm.add(txtDireccion);
        gridForm.add(lblCapacidad); gridForm.add(txtCapacidad);
        gridForm.add(lblTarifa); gridForm.add(txtTarifa);

        JButton btnGuardar = new JButton("💾 Guardar Parqueadero");
        estilizarBotonAccion(btnGuardar, COLOR_PRIMARIO);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(COLOR_DASHBOARD_CARD);
        btnPanel.add(btnGuardar);

        formContainer.add(gridForm, BorderLayout.CENTER);
        formContainer.add(btnPanel, BorderLayout.SOUTH);

        panel.add(formContainer, BorderLayout.NORTH);

        String[] cols = {"ID", "Nombre", "Dirección", "Capacidad", "Tarifa/Hora"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        estilizarTablaOscura(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> {
            try {
                if (txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "El nombre es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Parqueadero p = new Parqueadero();
                p.setNombre(txtNombre.getText().trim());
                p.setDireccion(txtDireccion.getText().trim());
                p.setCapacidad(Integer.parseInt(txtCapacidad.getText().trim()));
                p.setTarifaPorHora(Double.parseDouble(txtTarifa.getText().trim()));

                pDao.agregar(p);
                cargarParqueaderos(model);

                txtNombre.setText(""); txtDireccion.setText(""); txtCapacidad.setText(""); txtTarifa.setText("");

                JOptionPane.showMessageDialog(panel, "Parqueadero guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosDashboard(); // Refrescar dashboard
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cargarParqueaderos(model);
        return panel;
    }

    // ========================================================================
    // PESTAÑA 2: GESTIÓN DE ESPACIOS (CON VALIDACIÓN DE NO REPETICIÓN)
    // ========================================================================
    private JPanel crearPanelEspacios() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_FONDO_GENERAL);

        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.setBackground(COLOR_DASHBOARD_CARD);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel flowForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        flowForm.setBackground(COLOR_DASHBOARD_CARD);

        JLabel lblIdParq = new JLabel("ID Parqueadero:");
        lblIdParq.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblIdParq.setForeground(COLOR_TEXTO_CLARO);

        JTextField txtIdParq = new JTextField("1", 5);
        estilizarCampoTextoOscuro(txtIdParq);

        JLabel lblNumEspacio = new JLabel("Número de Espacio (Ej: A-01):");
        lblNumEspacio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNumEspacio.setForeground(COLOR_TEXTO_CLARO);

        JTextField txtNumEspacio = new JTextField(15);
        estilizarCampoTextoOscuro(txtNumEspacio);

        JButton btnCrear = new JButton("➕ Crear Espacio");
        estilizarBotonAccion(btnCrear, COLOR_PRIMARIO);

        flowForm.add(lblIdParq);
        flowForm.add(txtIdParq);
        flowForm.add(lblNumEspacio);
        flowForm.add(txtNumEspacio);
        flowForm.add(btnCrear);

        formContainer.add(flowForm, BorderLayout.NORTH);

        String[] colsE = {"ID Espacio", "ID Parqueadero", "Número", "Estado"};
        modelEspacios = new DefaultTableModel(colsE, 0);
        JTable tableE = new JTable(modelEspacios);
        estilizarTablaOscura(tableE);
        JScrollPane scrollE = new JScrollPane(tableE);
        scrollE.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1));
        panel.add(scrollE, BorderLayout.CENTER);

        btnCrear.addActionListener(e -> {
            try {
                String numEspacio = txtNumEspacio.getText().trim().toUpperCase();
                int idParq = Integer.parseInt(txtIdParq.getText().trim());

                if (numEspacio.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Ingrese un número de espacio.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // VALIDACIÓN: Verificar si el número de espacio ya existe en ese parqueadero
                List<Espacio> todosLosEspacios = eDao.listar();
                boolean existe = false;
                for (Espacio esp : todosLosEspacios) {
                    if (esp.getIdParqueadero() == idParq && esp.getNumeroEspacio().equalsIgnoreCase(numEspacio)) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    JOptionPane.showMessageDialog(panel, 
                        "⚠️ El espacio '" + numEspacio + "' ya existe en el parqueadero ID " + idParq + ".", 
                        "Espacio Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Espacio esp = new Espacio();
                esp.setIdParqueadero(idParq);
                esp.setNumeroEspacio(numEspacio);
                esp.setEstado(true);

                eDao.agregar(esp);
                cargarEspacios();

                txtNumEspacio.setText("");
                JOptionPane.showMessageDialog(panel, "Espacio creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosDashboard(); // Refrescar dashboard

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID Parqueadero debe ser un número.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error al crear espacio: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        cargarEspacios();
        return panel;
    }

    // ========================================================================
    // PESTAÑA 3: ENTRADA / SALIDA DE CARROS
    // ========================================================================
    private JPanel crearPanelCarros() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COLOR_FONDO_GENERAL);

        JPanel formContainer = new JPanel(new BorderLayout(10, 10));
        formContainer.setBackground(COLOR_DASHBOARD_CARD);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel gridForm = new JPanel(new GridLayout(2, 4, 15, 15));
        gridForm.setBackground(COLOR_DASHBOARD_CARD);

        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPlaca.setForeground(COLOR_TEXTO_CLARO);

        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMarca.setForeground(COLOR_TEXTO_CLARO);

        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblModelo.setForeground(COLOR_TEXTO_CLARO);

        JLabel lblColor = new JLabel("Color:");
        lblColor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblColor.setForeground(COLOR_TEXTO_CLARO);

        JTextField txtPlaca = new JTextField();
        JTextField txtMarca = new JTextField();
        JTextField txtModelo = new JTextField();
        JTextField txtColor = new JTextField();

        estilizarCampoTextoOscuro(txtPlaca);
        estilizarCampoTextoOscuro(txtMarca);
        estilizarCampoTextoOscuro(txtModelo);
        estilizarCampoTextoOscuro(txtColor);

        gridForm.add(lblPlaca); gridForm.add(txtPlaca);
        gridForm.add(lblMarca); gridForm.add(txtMarca);
        gridForm.add(lblModelo); gridForm.add(txtModelo);
        gridForm.add(lblColor); gridForm.add(txtColor);

        JButton btnIngresar = new JButton("✅ INGRESAR CARRO");
        estilizarBotonAccion(btnIngresar, COLOR_EXITO);

        JButton btnRetirar = new JButton("❌ RETIRAR SELECCIONADO");
        estilizarBotonAccion(btnRetirar, COLOR_PELIGRO);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(COLOR_DASHBOARD_CARD);
        btnPanel.add(btnIngresar);
        btnPanel.add(btnRetirar);

        gridForm.add(new JLabel());
        gridForm.add(new JLabel());
        gridForm.add(new JLabel());
        gridForm.add(btnPanel);

        formContainer.add(gridForm, BorderLayout.CENTER);
        panel.add(formContainer, BorderLayout.NORTH);

        String[] colsC = {"ID", "Placa", "Marca", "Modelo", "Color", "Espacio", "Fecha Ingreso"};
        modelCarros = new DefaultTableModel(colsC, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableCarros = new JTable(modelCarros);
        estilizarTablaOscura(tableCarros);
        JScrollPane scrollC = new JScrollPane(tableCarros);
        scrollC.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1));
        panel.add(scrollC, BorderLayout.CENTER);

        btnIngresar.addActionListener(e -> {
            String placa = txtPlaca.getText().trim().toUpperCase();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String color = txtColor.getText().trim();

            if (placa.isEmpty() || marca.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "La Placa y la Marca son obligatorias.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Espacio espacioLibre = eDao.buscarEspacioLibre(1);

                if (espacioLibre == null) {
                    JOptionPane.showMessageDialog(panel, "⚠️ ¡No hay espacios disponibles! El parqueadero está lleno.", "Parqueadero Lleno", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Carro nuevoCarro = new Carro();
                nuevoCarro.setPlaca(placa);
                nuevoCarro.setMarca(marca);
                nuevoCarro.setModelo(modelo);
                nuevoCarro.setColor(color);
                nuevoCarro.setIdEspacio(espacioLibre.getIdEspacio());
                nuevoCarro.setFechaIngreso(new Timestamp(new Date().getTime()));

                cDao.agregar(nuevoCarro);

                txtPlaca.setText(""); txtMarca.setText(""); txtModelo.setText(""); txtColor.setText("");

                cargarCarros();
                cargarEspacios();

                JOptionPane.showMessageDialog(panel,
                        "✅ Carro ingresado correctamente.\n" +
                                "Placa: " + placa + "\n" +
                                "Espacio asignado: " + espacioLibre.getNumeroEspacio(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatosDashboard(); // Refrescar dashboard

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(panel, "Error al ingresar carro: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnRetirar.addActionListener(e -> {
            int filaSeleccionada = tableCarros.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panel, "Seleccione un carro de la tabla para retirarlo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idCarro = (int) modelCarros.getValueAt(filaSeleccionada, 0);
            int idEspacio = (int) modelCarros.getValueAt(filaSeleccionada, 5);
            String placa = (String) modelCarros.getValueAt(filaSeleccionada, 1);

            int confirm = JOptionPane.showConfirmDialog(panel,
                    "¿Está seguro de retirar el carro con placa: " + placa + "?\nSe liberará el espacio automáticamente.",
                    "Confirmar Salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    cDao.eliminar(idCarro, idEspacio);
                    cargarCarros();
                    cargarEspacios();

                    JOptionPane.showMessageDialog(panel,
                            "🚗 Carro retirado exitosamente.\n" +
                                    "Espacio " + idEspacio + " ha sido liberado.",
                            "Salida Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    cargarDatosDashboard(); // Refrescar dashboard

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error al retirar carro: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        cargarCarros();
        return panel;
    }

    // ========================================================================
    // PESTAÑA 4: HISTORIAL (Placeholder Mejorado)
    // ========================================================================
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO_GENERAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea areaInfo = new JTextArea();
        areaInfo.setEditable(false);
        areaInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        areaInfo.setBackground(COLOR_FONDO_GENERAL);
        areaInfo.setForeground(COLOR_TEXTO_CLARO);
        areaInfo.setText("MÓDULO DE HISTORIAL\n\n" +
                "Aquí se mostrarán todos los movimientos de entrada y salida,\n" +
                "junto con el cálculo de tarifas y tiempo de estancia.\n\n" +
                "Para implementarlo completamente, necesitarías:\n" +
                "- Una tabla 'movimientos' en la base de datos.\n" +
                "- Un MovimientoDAO para registrar entradas/salidas.\n" +
                "- Calcular el total a pagar basado en la tarifa del parqueadero y el tiempo transcurrido.\n\n" +
                "Este módulo puede ser desarrollado como mejora futura.");

        JScrollPane scroll = new JScrollPane(areaInfo);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ========================================================================
    // MÉTODOS AUXILIARES DE ESTILO OSCURO
    // ========================================================================
    private void estilizarCampoTextoOscuro(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(COLOR_CAMPO_INPUT);
        field.setForeground(COLOR_TEXTO_CLARO);
    }

    private void estilizarBotonAccion(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 35));
    }

    private void estilizarTablaOscura(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(COLOR_TABLA_HEADER_BG);
        header.setForeground(COLOR_TABLA_HEADER_TEXT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDE_SUAVE));

        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setSelectionBackground(COLOR_PRIMARIO);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(COLOR_BORDE_SUAVE);
        table.setBackground(COLOR_DASHBOARD_CARD);
        table.setForeground(COLOR_TEXTO_CLARO);
    }

    // ========================================================================
    // MÉTODOS DE CARGA DE DATOS
    // ========================================================================
    private void cargarParqueaderos(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Parqueadero> lista = pDao.listar();
            for (Parqueadero p : lista) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getDireccion(),
                        p.getCapacidad(),
                        "$" + String.format("%.2f", p.getTarifaPorHora())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar parqueaderos: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void cargarEspacios() {
        if (modelEspacios == null) return;
        modelEspacios.setRowCount(0);
        try {
            List<Espacio> lista = eDao.listar();
            for (Espacio e : lista) {
                modelEspacios.addRow(new Object[]{
                        e.getIdEspacio(),
                        e.getIdParqueadero(),
                        e.getNumeroEspacio(),
                        e.isEstado() ? "LIBRE" : "OCUPADO"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar espacios: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public void cargarCarros() {
        if (modelCarros == null) return;
        modelCarros.setRowCount(0);
        try {
            List<Carro> lista = cDao.listar();
            for (Carro c : lista) {
                modelCarros.addRow(new Object[]{
                        c.getIdCarro(),
                        c.getPlaca(),
                        c.getMarca(),
                        c.getModelo(),
                        c.getColor(),
                        c.getIdEspacio(),
                        c.getFechaIngreso() != null ? c.getFechaIngreso().toString() : "N/A"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar carros: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}