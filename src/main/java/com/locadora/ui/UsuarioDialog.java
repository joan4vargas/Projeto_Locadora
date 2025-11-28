package com.locadora.ui;

import com.locadora.model.Usuario;
import com.locadora.service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class UsuarioDialog extends JDialog {

    private final UsuarioService service = new UsuarioService();

    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone;

    private JButton btnSalvar;
    private boolean saved = false;

    private Usuario usuarioEdicao;

    // ==========================================================
    // CONSTRUTORES
    // ==========================================================
    public UsuarioDialog(Frame parent) {
        super(parent, "Novo Usuário", true);
        initComponents();
    }

    public UsuarioDialog(Frame parent, Long idEdicao) {
        super(parent, "Editar Usuário", true);
        this.usuarioEdicao = service.findById(idEdicao);
        initComponents();
        carregarDados();
    }

    // ==========================================================
    // UI
    // ==========================================================
    private void initComponents() {
        setSize(400, 260);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtNome = new JTextField();
        txtEmail = new JTextField();
        txtTelefone = new JTextField();

        form.add(new JLabel("Nome:"));
        form.add(txtNome);

        form.add(new JLabel("Email:"));
        form.add(txtEmail);

        form.add(new JLabel("Telefone:"));
        form.add(txtTelefone);

        add(form, BorderLayout.CENTER);

        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());

        JPanel botoes = new JPanel();
        botoes.add(btnSalvar);
        add(botoes, BorderLayout.SOUTH);
    }

    private void carregarDados() {
        if (usuarioEdicao == null) return;

        txtNome.setText(usuarioEdicao.getNome());
        txtEmail.setText(usuarioEdicao.getEmail());
        txtTelefone.setText(usuarioEdicao.getTelefone());
    }

    // ==========================================================
    // VALIDAÇÕES
    // ==========================================================

    private boolean validarNome(String nome) {
        return nome.matches("^[A-Za-zÀ-ú ]{3,}$");
    }

    private boolean validarEmail(String email) {
        String regexEmail =
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regexEmail, email);
    }

    private boolean validarTelefone(String telefone) {
        // Aceita: (47) 99999-0000 OU 47999990000
        String regexTelefone =
                "^\\(?(\\d{2})\\)?\\s?\\d{4,5}-?\\d{4}$";
        return Pattern.matches(regexTelefone, telefone);
    }

    // ==========================================================
    // SALVAR
    // ==========================================================
    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String telefone = txtTelefone.getText().trim();

            if (!validarNome(nome)) {
                JOptionPane.showMessageDialog(this,
                        "Nome inválido.\nUse apenas letras e espaços (mínimo 3 caracteres).",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!validarEmail(email)) {
                JOptionPane.showMessageDialog(this,
                        "Email inválido.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!validarTelefone(telefone)) {
                JOptionPane.showMessageDialog(this,
                        "Telefone inválido.\nUse formatos:\n(47) 99999-0000\n47999990000",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuarioEdicao == null) {
                usuarioEdicao = new Usuario(nome, email, telefone);
            } else {
                usuarioEdicao.setNome(nome);
                usuarioEdicao.setEmail(email);
                usuarioEdicao.setTelefone(telefone);
            }

            service.save(usuarioEdicao);
            saved = true;

            JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar usuário:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ==========================================================
    // INDICADOR DE SUCESSO
    // ==========================================================
    public boolean isSaved() {
        return saved;
    }
}
