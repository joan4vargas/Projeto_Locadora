package com.locadora.main;

import com.formdev.flatlaf.FlatLightLaf;
import com.locadora.init.DataLoader;
import com.locadora.ui.TelaPrincipal;

import javax.swing.*;

public class PontoDeEntrada {
    public static void main(String[] args) {

        FlatLightLaf.setup();  // Permite troca dinâmica de tema

        DataLoader.seed();

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal t = new TelaPrincipal();
            t.setLocationRelativeTo(null);
            t.setVisible(true);
        });
    }
}
