// Main.java (en paquete vista)
package vista;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaPrincipal().setVisible(true);
        });
    }
}