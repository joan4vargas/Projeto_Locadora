package com.locadora.ui.renderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StatusIconRenderer extends DefaultTableCellRenderer {

    private final Map<String, Color> statusCores = new HashMap<>();

    public StatusIconRenderer() {
        statusCores.put("ABERTO", new Color(0, 180, 0));       // Verde
        statusCores.put("FECHADO", new Color(200, 0, 0));      // Vermelho
        statusCores.put("ATRASADO", new Color(230, 180, 0));   // Amarelo
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        String status = value == null ? "" : value.toString().trim().toUpperCase();

        Color cor = statusCores.get(status);

        lbl.setText("");

        if (cor != null) {
            lbl.setIcon(criarBolinha(cor));
        } else {
            lbl.setIcon(null);
        }

        lbl.setHorizontalAlignment(SwingConstants.CENTER);

        // Mantém visual FlatLaf consistente
        if (isSelected) {
            lbl.setBackground(table.getSelectionBackground());
            lbl.setForeground(table.getSelectionForeground());
        } else {
            lbl.setBackground(table.getBackground());
            lbl.setForeground(table.getForeground());
        }

        return lbl;
    }

    // ------------------------------------------------------------
    // Cria bolinha colorida (círculo)
    // ------------------------------------------------------------
    private Icon criarBolinha(Color c) {
        int size = 14;

        return new Icon() {
            @Override
            public void paintIcon(Component comp, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(c);
                g2.fillOval(x, y, size, size);

                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(x, y, size, size);

                g2.dispose();
            }

            @Override public int getIconWidth() { return size; }
            @Override public int getIconHeight() { return size; }
        };
    }
}
