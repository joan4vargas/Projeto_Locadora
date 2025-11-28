package com.locadora.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.locadora.model.Aluguel;
import com.locadora.model.Usuario;
import com.locadora.model.Veiculo;
import com.locadora.service.AluguelService;
import com.locadora.service.UsuarioService;
import com.locadora.service.VeiculoService;
import com.locadora.ui.renderers.ColorCellRenderer;
import com.locadora.ui.renderers.StatusIconRenderer;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private final UsuarioService usuarioService = new UsuarioService();
    private final VeiculoService veiculoService = new VeiculoService();
    private final AluguelService aluguelService = new AluguelService();

    private final DefaultTableModel modelUsuarios =
            new DefaultTableModel(new Object[]{"ID", "Nome", "Email", "Telefone"}, 0);

    private final DefaultTableModel modelVeiculos =
            new DefaultTableModel(new Object[]{"ID", "Placa", "Marca", "Modelo", "Ano", "Cor"}, 0);

    private final DefaultTableModel modelAlugueis =
            new DefaultTableModel(new Object[]{"ID", "Cliente", "Veículo", "Início", "Fim", "Status", "KM Inicial", "KM Final"}, 0);

    private final JTable tblUsuarios = new JTable(modelUsuarios);
    private final JTable tblVeiculos = new JTable(modelVeiculos);
    private final JTable tblAlugueis = new JTable(modelAlugueis);

    private boolean darkMode = false;

    public TelaPrincipal() {
        setTitle("Locadora de Veículos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 600);
        setLayout(new BorderLayout());

        add(buildThemeToggle(), BorderLayout.NORTH);

        initComponents();
        loadAll();
    }

    // -----------------------------------------------------------
    //   BOTÃO TEMA
    // -----------------------------------------------------------
    private JPanel buildThemeToggle() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        p.setOpaque(false);

        JToggleButton toggle = new JToggleButton("☀️");
        toggle.setPreferredSize(new Dimension(70, 28));
        toggle.setFocusable(false);

        toggle.addActionListener(e -> {
            darkMode = toggle.isSelected();

            try {
                if (darkMode) {
                    FlatDarkLaf.setup();
                    toggle.setText("🌙");
                } else {
                    FlatLightLaf.setup();
                    toggle.setText("☀️");
                }
                SwingUtilities.updateComponentTreeUI(this);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        p.add(toggle);
        return p;
    }

    // -----------------------------------------------------------
    //   TABS E TABELAS
    // -----------------------------------------------------------
    private void initComponents() {
        JTabbedPane tabs = new JTabbedPane();

        // ---------------- USUÁRIOS ----------------
        JPanel pU = new JPanel(new BorderLayout());
        tblUsuarios.setAutoCreateRowSorter(true);
        pU.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);

        JPanel btnsU = new JPanel();
        JButton btAddU = new JButton("Adicionar");
        JButton btEditU = new JButton("Editar");
        JButton btDelU = new JButton("Excluir");
        btnsU.add(btAddU);
        btnsU.add(btEditU);
        btnsU.add(btDelU);
        pU.add(btnsU, BorderLayout.SOUTH);

        btAddU.addActionListener(e -> {
            UsuarioDialog dlg = new UsuarioDialog(this);
            dlg.setVisible(true);
            if (dlg.isSaved()) loadUsuarios();
        });

        btEditU.addActionListener(e -> {
            int row = tblUsuarios.getSelectedRow();
            if (row < 0) return;

            Long id = (Long) modelUsuarios.getValueAt(tblUsuarios.convertRowIndexToModel(row), 0);

            UsuarioDialog dlg = new UsuarioDialog(this, id);
            dlg.setVisible(true);
            if (dlg.isSaved()) loadUsuarios();
        });

        btDelU.addActionListener(e -> {
            int row = tblUsuarios.getSelectedRow();
            if (row < 0) return;

            Long id = (Long) modelUsuarios.getValueAt(tblUsuarios.convertRowIndexToModel(row), 0);
            usuarioService.delete(id);
            loadUsuarios();
        });


        // ---------------- VEÍCULOS ----------------
        JPanel pV = new JPanel(new BorderLayout());
        tblVeiculos.setAutoCreateRowSorter(true);
        tblVeiculos.getColumnModel().getColumn(5).setCellRenderer(new ColorCellRenderer());
        pV.add(new JScrollPane(tblVeiculos), BorderLayout.CENTER);

        JPanel btnsV = new JPanel();
        JButton btAddV = new JButton("Adicionar");
        JButton btEditV = new JButton("Editar");
        JButton btDelV = new JButton("Excluir");
        btnsV.add(btAddV);
        btnsV.add(btEditV);
        btnsV.add(btDelV);
        pV.add(btnsV, BorderLayout.SOUTH);

        btAddV.addActionListener(e -> {
            VeiculoDialog dlg = new VeiculoDialog(this);
            dlg.setVisible(true);
            if (dlg.isSaved()) loadVeiculos();
        });

        btEditV.addActionListener(e -> {
            int row = tblVeiculos.getSelectedRow();
            if (row < 0) return;

            Long id = (Long) modelVeiculos.getValueAt(tblVeiculos.convertRowIndexToModel(row), 0);

            VeiculoDialog dlg = new VeiculoDialog(this, id);
            dlg.setVisible(true);
            if (dlg.isSaved()) loadVeiculos();
        });

        btDelV.addActionListener(e -> {
            int row = tblVeiculos.getSelectedRow();
            if (row < 0) return;

            Long id = (Long) modelVeiculos.getValueAt(tblVeiculos.convertRowIndexToModel(row), 0);
            veiculoService.delete(id);
            loadVeiculos();
        });


        // ---------------- ALUGUÉIS ----------------
        JPanel pA = new JPanel(new BorderLayout());
        tblAlugueis.setAutoCreateRowSorter(true);
        tblAlugueis.getColumnModel().getColumn(5).setCellRenderer(new StatusIconRenderer());
        pA.add(new JScrollPane(tblAlugueis), BorderLayout.CENTER);

        JPanel btnsA = new JPanel();
        JButton btAbrir = new JButton("Abrir Aluguel");
        JButton btFechar = new JButton("Fechar Aluguel");
        JButton btResumo = new JButton("Resumo");
        JButton btPdf = new JButton("Gerar PDF");
        JButton btExcluir = new JButton("Excluir");

        btnsA.add(btAbrir);
        btnsA.add(btFechar);
        btnsA.add(btResumo);
        btnsA.add(btPdf);
        btnsA.add(btExcluir);

        pA.add(btnsA, BorderLayout.SOUTH);

        // ---- AÇÕES ----

        btAbrir.addActionListener(e -> {
            AluguelDialog dlg = new AluguelDialog(this);
            dlg.setVisible(true);

            if (dlg.isSaved()) loadAlugueis();
        });


        // ---------- FECHAR ALUGUEL ----------
        btFechar.addActionListener(e -> {
            int row = tblAlugueis.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um aluguel");
                return;
            }

            Long id = (Long) modelAlugueis.getValueAt(tblAlugueis.convertRowIndexToModel(row), 0);
            Aluguel aluguelSelecionado = aluguelService.findById(id);

            if (aluguelSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Aluguel não encontrado!");
                return;
            }

            AluguelDialog dlg = new AluguelDialog(this, aluguelSelecionado, true);
            dlg.setVisible(true);

            if (dlg.isSaved()) loadAlugueis();
        });


        // ---------- RESUMO ----------
        btResumo.addActionListener(e -> {
            int row = tblAlugueis.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um aluguel");
                return;
            }

            Long id = (Long) modelAlugueis.getValueAt(tblAlugueis.convertRowIndexToModel(row), 0);
            Aluguel aluguelSelecionado = aluguelService.findById(id);

            if (aluguelSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Aluguel não encontrado!");
                return;
            }

            AluguelDialog dlg = new AluguelDialog(this, aluguelSelecionado, false);
            dlg.mostrarPopupResumoDireto();
        });


        // ---------- GERAR PDF ----------
        btPdf.addActionListener(e -> {
            int row = tblAlugueis.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um aluguel");
                return;
            }

            Long id = (Long) modelAlugueis.getValueAt(tblAlugueis.convertRowIndexToModel(row), 0);
            Aluguel aluguel = aluguelService.findById(id);

            if (aluguel == null) {
                JOptionPane.showMessageDialog(this, "Aluguel não encontrado!");
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Salvar PDF");
            chooser.setSelectedFile(new File("resumo-aluguel.pdf"));

            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File file = chooser.getSelectedFile();

            try {
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream c = new PDPageContentStream(document, page);

                c.beginText();
                c.setFont(PDType1Font.HELVETICA_BOLD, 18);
                c.newLineAtOffset(60, 750);
                c.showText("Resumo do Aluguel");
                c.endText();

                c.beginText();
                c.setFont(PDType1Font.HELVETICA, 12);
                c.newLineAtOffset(60, 720);

                c.showText("Cliente: " + aluguel.getUsuario().getNome());
                c.newLineAtOffset(0, -20);
                c.showText("Veículo: " + aluguel.getVeiculo());
                c.newLineAtOffset(0, -20);
                c.showText("Data Início: " + aluguel.getDataInicio());
                c.newLineAtOffset(0, -20);
                c.showText("Data Fim: " + aluguel.getDataFim());
                c.newLineAtOffset(0, -20);
                c.showText("KM Inicial: " + aluguel.getQuilometragemInicial());
                c.newLineAtOffset(0, -20);
                c.showText("KM Final: " + aluguel.getQuilometragemFinal());

                c.endText();
                c.close();

                document.save(file);
                document.close();

                JOptionPane.showMessageDialog(this, "PDF gerado com sucesso!");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao gerar PDF: " + ex.getMessage());
            }
        });


        // ---------- EXCLUIR ----------
        btExcluir.addActionListener(e -> {
            int row = tblAlugueis.getSelectedRow();
            if (row < 0) return;

            Long id = (Long) modelAlugueis.getValueAt(tblAlugueis.convertRowIndexToModel(row), 0);

            aluguelService.delete(id);
            loadAlugueis();
        });


        tabs.addTab("Usuários", pU);
        tabs.addTab("Veículos", pV);
        tabs.addTab("Aluguéis", pA);

        add(tabs, BorderLayout.CENTER);
    }

    // -----------------------------------------------------------
    //   MÉTODOS DE CARREGAMENTO
    // -----------------------------------------------------------
    private void loadAll() {
        loadUsuarios();
        loadVeiculos();
        loadAlugueis();
    }

    private void loadUsuarios() {
        modelUsuarios.setRowCount(0);
        for (Usuario u : usuarioService.listAll()) {
            modelUsuarios.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getTelefone()});
        }
    }

    private void loadVeiculos() {
        modelVeiculos.setRowCount(0);
        for (Veiculo v : veiculoService.listAll()) {
            modelVeiculos.addRow(new Object[]{v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getAno(), v.getCor()});
        }
    }

    private void loadAlugueis() {
        modelAlugueis.setRowCount(0);
        for (Aluguel a : aluguelService.listAll()) {
            modelAlugueis.addRow(new Object[]{
                    a.getId(),
                    a.getUsuario() != null ? a.getUsuario().getNome() : "",
                    a.getVeiculo() != null ? a.getVeiculo().toString() : "",
                    a.getDataInicio(),
                    a.getDataFim(),
                    a.getStatus(),
                    a.getQuilometragemInicial(),
                    a.getQuilometragemFinal()
            });
        }
    }
}
