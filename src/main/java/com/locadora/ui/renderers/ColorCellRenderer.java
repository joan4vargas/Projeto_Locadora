package com.locadora.ui.renderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorCellRenderer extends DefaultTableCellRenderer {

    private final Map<String, Color> corMap = new HashMap<>();

    public ColorCellRenderer() {

        // ======= MAPA DE CORES ============
        corMap.put("preto", Color.BLACK);
        corMap.put("branco", Color.WHITE);
        corMap.put("vermelho", Color.RED);
        corMap.put("azul", Color.BLUE);
        corMap.put("verde", Color.GREEN);
        corMap.put("amarelo", Color.YELLOW);
        corMap.put("laranja", Color.ORANGE);
        corMap.put("rosa", Color.PINK);

        // Cores comuns em carros
        corMap.put("prata", new Color(192, 192, 192));
        corMap.put("cinza", new Color(128, 128, 128));
        corMap.put("grafite", new Color(64, 64, 64));
        corMap.put("vinho", new Color(128, 0, 64));
        corMap.put("marrom", new Color(120, 72, 0));
        corMap.put("bege", new Color(245, 245, 220));
        corMap.put("champagne", new Color(250, 235, 210));

        // fallback se vier em maiúsculas
        Map<String, Color> extras = new HashMap<>();
        for (String key : corMap.keySet()) {
            extras.put(key.toUpperCase(), corMap.get(key));
        }
        corMap.putAll(extras);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        String corStr = value == null ? "" : value.toString().trim().toLowerCase();

        Color cor = corMap.getOrDefault(corStr, null);

        if (cor != null) {
            c.setBackground(cor);

            // texto branco se a cor for escura
            c.setForeground(isCorEscura(cor) ? Color.WHITE : Color.BLACK);

        } else {
            // caso a cor não exista no map
            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        }

        return c;
    }

    // ------------------------------------------------------------
    // Função auxiliar: verifica se uma cor é escura
    // ------------------------------------------------------------
    private boolean isCorEscura(Color c) {
        double luminancia = (0.299 * c.getRed() +
                             0.587 * c.getGreen() +
                             0.114 * c.getBlue()) / 255;
        return luminancia < 0.5;
    }
}
