package com.locadora.ui;

import com.locadora.model.Aluguel;
import com.locadora.model.Usuario;
import com.locadora.model.Veiculo;
import com.locadora.service.AluguelService;
import com.locadora.service.UsuarioService;
import com.locadora.service.VeiculoService;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class AluguelDialog extends JDialog {

    private JComboBox<Usuario> cbUsuario;
    private JComboBox<Veiculo> cbVeiculo;
    private DatePicker dpDataInicio;
    private DatePicker dpDataFim;
    private JTextField txtKmInicial;
    private JTextField txtKmFinal;
    private JLabel lblKmFinal;

    private final AluguelService aluguelService = new AluguelService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final VeiculoService veiculoService = new VeiculoService();

    private Aluguel aluguel;
    private boolean modoFechar = false;

    private boolean saved = false; // controle de salvamento

    // ==========================
    //   CONSTRUTOR: ABRIR NOVO
    // ==========================
    public AluguelDialog(Frame owner) {
        super(owner, "Abrir Aluguel", true);
        initComponents();
        carregarCombos();

        dpDataInicio.setDate(LocalDate.now());
        dpDataFim.setDate(LocalDate.now());
    }

    // ==========================
    //   CONSTRUTOR: FECHAR / RESUMO
    // ==========================
    public AluguelDialog(Frame owner, Aluguel aluguel, boolean fechar) {
        super(owner, fechar ? "Fechar Aluguel" : "Resumo do Aluguel", true);

        this.aluguel = aluguel;
        this.modoFechar = fechar;

        initComponents();
        carregarCombos();
        preencherCamposDoAluguel();

        ajustarCamposParaModo();
    }

    // ========================================================
    //  COMPONENTES
    // ========================================================
    private void initComponents() {

        setSize(420, 380);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblUsuario = new JLabel("Cliente:");
        lblUsuario.setBounds(30, 20, 120, 25);
        add(lblUsuario);

        cbUsuario = new JComboBox<>();
        cbUsuario.setBounds(150, 20, 220, 25);
        add(cbUsuario);

        JLabel lblVeiculo = new JLabel("Veículo:");
        lblVeiculo.setBounds(30, 60, 120, 25);
        add(lblVeiculo);

        cbVeiculo = new JComboBox<>();
        cbVeiculo.setBounds(150, 60, 220, 25);
        add(cbVeiculo);

        JLabel lblInicio = new JLabel("Início:");
        lblInicio.setBounds(30, 100, 120, 25);
        add(lblInicio);

        dpDataInicio = new DatePicker();
        dpDataInicio.setBounds(150, 100, 160, 25);
        add(dpDataInicio);

        JLabel lblFim = new JLabel("Fim:");
        lblFim.setBounds(30, 140, 120, 25);
        add(lblFim);

        dpDataFim = new DatePicker();
        dpDataFim.setBounds(150, 140, 160, 25);
        add(dpDataFim);

        JLabel lblKmIni = new JLabel("KM Inicial:");
        lblKmIni.setBounds(30, 180, 120, 25);
        add(lblKmIni);

        txtKmInicial = new JTextField();
        txtKmInicial.setBounds(150, 180, 120, 25);
        add(txtKmInicial);

        lblKmFinal = new JLabel("KM Final:");
        lblKmFinal.setBounds(30, 220, 120, 25);
        add(lblKmFinal);

        txtKmFinal = new JTextField();
        txtKmFinal.setBounds(150, 220, 120, 25);
        add(txtKmFinal);

        lblKmFinal.setVisible(false);
        txtKmFinal.setVisible(false);

        // BOTÃO COMPORTA-SE DIFERENTE SE FOR FECHAMENTO
        JButton btSalvar = new JButton(modoFechar ? "OK" : "Salvar");
        btSalvar.setBounds(70, 270, 110, 30);
        btSalvar.addActionListener(e -> salvar());
        add(btSalvar);

        JButton btCancelar = new JButton("Cancelar");
        btCancelar.setBounds(210, 270, 110, 30);
        btCancelar.addActionListener(e -> dispose());
        add(btCancelar);
    }

    // ========================================================
    //  CARREGAR COMBOS
    // ========================================================
    private void carregarCombos() {
        List<Usuario> usuarios = usuarioService.listAll();
        for (Usuario u : usuarios) cbUsuario.addItem(u);

        List<Veiculo> veiculos = veiculoService.listAll();
        for (Veiculo v : veiculos) cbVeiculo.addItem(v);
    }

    // ========================================================
    //  PREENCHER CAMPOS
    // ========================================================
    private void preencherCamposDoAluguel() {
        cbUsuario.setSelectedItem(aluguel.getUsuario());
        cbVeiculo.setSelectedItem(aluguel.getVeiculo());

        dpDataInicio.setDate(aluguel.getDataInicio());
        dpDataFim.setDate(aluguel.getDataFim());

        txtKmInicial.setText(String.valueOf(aluguel.getQuilometragemInicial()));

        if (aluguel.getQuilometragemFinal() != null)
            txtKmFinal.setText(String.valueOf(aluguel.getQuilometragemFinal()));
    }

    // ========================================================
    //  AJUSTE PARA MODO FECHAR
    // ========================================================
    private void ajustarCamposParaModo() {

        if (modoFechar) {

            cbUsuario.setEnabled(false);
            cbVeiculo.setEnabled(false);

            dpDataInicio.setEnabled(false);
            dpDataFim.setEnabled(true);

            txtKmInicial.setEnabled(false);

            lblKmFinal.setVisible(true);
            txtKmFinal.setVisible(true);
            txtKmFinal.setEnabled(true);
        }
    }

    // ========================================================
    //  VALIDAÇÃO
    // ========================================================
    private boolean validarCampos(boolean fechar) {

        if (cbUsuario.getSelectedItem() == null) {
            mensagem("Selecione um cliente.");
            return false;
        }

        if (cbVeiculo.getSelectedItem() == null) {
            mensagem("Selecione um veículo.");
            return false;
        }

        if (dpDataInicio.getDate() == null || dpDataFim.getDate() == null) {
            mensagem("Preencha as datas.");
            return false;
        }

        try {
            Integer.parseInt(txtKmInicial.getText());
        } catch (Exception e) {
            mensagem("KM Inicial inválido.");
            return false;
        }

        if (fechar) {
            try {
                Integer.parseInt(txtKmFinal.getText());
            } catch (Exception e) {
                mensagem("KM Final inválido.");
                return false;
            }
        }

        return true;
    }

    private void mensagem(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    // ========================================================
    //  SALVAMENTO
    // ========================================================
    private void salvar() {

        if (modoFechar) {
            salvarFechamento();
        } else {
            salvarAbertura();
        }
    }

    private void salvarAbertura() {

        if (!validarCampos(false)) return;

        Aluguel a = new Aluguel();
        a.setUsuario((Usuario) cbUsuario.getSelectedItem());
        a.setVeiculo((Veiculo) cbVeiculo.getSelectedItem());
        a.setDataInicio(dpDataInicio.getDate());
        a.setDataFim(dpDataFim.getDate());
        a.setStatus("ABERTO");
        a.setQuilometragemInicial(Integer.parseInt(txtKmInicial.getText()));

        aluguelService.save(a);
        saved = true;

        mensagem("Aluguel aberto com sucesso!");
        dispose();
    }

    private void salvarFechamento() {

        if (!validarCampos(true)) return;

        aluguel.setDataFim(dpDataFim.getDate());
        aluguel.setQuilometragemFinal(Integer.parseInt(txtKmFinal.getText()));
        aluguel.setStatus("FECHADO");

        aluguelService.save(aluguel);
        saved = true;

        mensagem("Aluguel fechado com sucesso!");
        dispose();
    }

    // ========================================================
    //   MÉTODOS PARA TELA PRINCIPAL
    // ========================================================
    public boolean isSaved() {
        return saved;
    }

    public void mostrarPopupResumoDireto() {

        if (aluguel == null) return;

        JOptionPane.showMessageDialog(this,
                "Cliente: " + aluguel.getUsuario().getNome() + "\n" +
                "Veículo: " + aluguel.getVeiculo() + "\n" +
                "Início: " + aluguel.getDataInicio() + "\n" +
                "Fim: " + aluguel.getDataFim() + "\n" +
                "KM Inicial: " + aluguel.getQuilometragemInicial() + "\n" +
                "KM Final: " + aluguel.getQuilometragemFinal(),
                "Resumo do Aluguel",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
