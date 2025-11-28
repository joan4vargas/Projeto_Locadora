package com.locadora.ui;

import com.locadora.model.Veiculo;
import com.locadora.service.VeiculoService;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class VeiculoDialog extends JDialog {

    private final VeiculoService service = new VeiculoService();
    private Veiculo veiculo;
    private boolean saved = false;

    private JTextField txtPlaca;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JSpinner spAno;
    private JComboBox<String> cbCor;

    private static final Map<String, Color> CORES = new LinkedHashMap<>();
    static {
        CORES.put("Preto", Color.BLACK);
        CORES.put("Branco", Color.WHITE);
        CORES.put("Vermelho", Color.RED);
        CORES.put("Azul", Color.BLUE);
        CORES.put("Prata", new Color(180, 180, 180));
        CORES.put("Cinza", new Color(120, 120, 120));
        CORES.put("Verde", new Color(0, 130, 0));
        CORES.put("Amarelo", Color.YELLOW);
        CORES.put("Marrom", new Color(120, 70, 20));
    }

    public VeiculoDialog(Frame owner) {
        super(owner, "Novo Veículo", true);
        initComponents();
    }

    public VeiculoDialog(Frame owner, Long id) {
        super(owner, "Editar Veículo", true);
        veiculo = service.findById(id);
        initComponents();
        loadData();
    }

    public boolean isSaved() {
        return saved;
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLayout(new BorderLayout());
        setLocationRelativeTo(getOwner());

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        form.add(new JLabel("Placa:"));
        txtPlaca = new JTextField();
        form.add(txtPlaca);

        form.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        form.add(txtMarca);

        form.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        form.add(txtModelo);

        form.add(new JLabel("Ano:"));
        spAno = new JSpinner(new SpinnerNumberModel(2024, 1900, 2100, 1));

        // REMOVE O PONTO DO ANO
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spAno, "####");
        spAno.setEditor(editor);

        form.add(spAno);

        form.add(new JLabel("Cor:"));
        cbCor = new JComboBox<>(CORES.keySet().toArray(new String[0]));
        form.add(cbCor);

        add(form, BorderLayout.CENTER);

        JButton btSalvar = new JButton("Salvar");
        btSalvar.addActionListener(e -> salvar());
        JPanel panelBtn = new JPanel();
        panelBtn.add(btSalvar);

        add(panelBtn, BorderLayout.SOUTH);
    }

    private void loadData() {
        if (veiculo != null) {
            txtPlaca.setText(veiculo.getPlaca());
            txtMarca.setText(veiculo.getMarca());
            txtModelo.setText(veiculo.getModelo());
            spAno.setValue(veiculo.getAno());
            cbCor.setSelectedItem(veiculo.getCor());
        }
    }

    private void salvar() {

        String placa = txtPlaca.getText().trim().toUpperCase();
        String marca = txtMarca.getText().trim();
        String modelo = txtModelo.getText().trim();
        int ano = (int) spAno.getValue();
        String cor = cbCor.getSelectedItem().toString();

        // -----------------------------
        // REGEX DE PLACA: MERCOSUL + ANTIGA
        // -----------------------------
        String regexPlaca = "^[A-Z]{3}-?\\d{4}$|^[A-Z]{3}[0-9][A-Z][0-9]{2}$";

        if (!placa.matches(regexPlaca)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Placa inválida!\nFormatos aceitos:\n- ABC-1234 (antiga)\n- ABC1D23 (Mercosul)",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (marca.isEmpty() || modelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (veiculo == null) {
            veiculo = new Veiculo();
        }

        veiculo.setPlaca(placa);
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setAno(ano);
        veiculo.setCor(cor);

        service.save(veiculo);

        saved = true;
        dispose();
    }
}
