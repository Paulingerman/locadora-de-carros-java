import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LocadoraGUI {

    static class Carro {
        private String placa;
        private String modelo;
        private Cliente alugadoPor;

        public Carro(String placa, String modelo) {
            this.placa = placa;
            this.modelo = modelo;
            this.alugadoPor = null;
        }

        public String getPlaca() {
            return placa;
        }

        public String getModelo() {
            return modelo;
        }

        public Cliente getAlugadoPor() {
            return alugadoPor;
        }

        public void alugar(Cliente cliente) {
            this.alugadoPor = cliente;
        }

        public void devolver() {
            this.alugadoPor = null;
        }

        @Override
        public String toString() {
            if (alugadoPor == null) {
                return "Carro: " + modelo + " (Placa: " + placa + ") - Disponível";
            } else {
                return "Carro: " + modelo + " (Placa: " + placa + ") - Alugado por: " + alugadoPor.getNome();
            }
        }
    }

    static class Cliente {
        private String id;
        private String nome;

        public Cliente(String id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public String getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        @Override
        public String toString() {
            return "Cliente: " + nome + " (ID: " + id + ")";
        }
    }

    private List<Carro> carros = new ArrayList<>();
    private List<Cliente> clientes = new ArrayList<>();
    private JFrame frame;
    private JTextArea textArea;

    public LocadoraGUI() {
        frame = new JFrame("Locadora de Carros");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Painel de botões
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 5, 5));
        
        JButton addCarButton = new JButton("Adicionar Carro");
        JButton addClientButton = new JButton("Registrar Cliente");
        JButton rentCarButton = new JButton("Alugar Carro");
        JButton returnCarButton = new JButton("Devolver Carro");
        JButton listButton = new JButton("Listar Carros");
        JButton reportButton = new JButton("Gerar Relatório");
        
        buttonPanel.add(addCarButton);
        buttonPanel.add(addClientButton);
        buttonPanel.add(rentCarButton);
        buttonPanel.add(returnCarButton);
        buttonPanel.add(listButton);
        buttonPanel.add(reportButton);
        
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Área de texto
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Ações dos botões
        addCarButton.addActionListener(e -> adicionarCarro());
        addClientButton.addActionListener(e -> registrarCliente());
        rentCarButton.addActionListener(e -> alugarCarro());
        returnCarButton.addActionListener(e -> devolverCarro());
        listButton.addActionListener(e -> listarCarros());
        reportButton.addActionListener(e -> gerarRelatorio());

        frame.setVisible(true);
    }

    private void adicionarCarro() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField placaField = new JTextField();
        JTextField modeloField = new JTextField();
        
        panel.add(new JLabel("Placa:"));
        panel.add(placaField);
        panel.add(new JLabel("Modelo:"));
        panel.add(modeloField);
        
        int result = JOptionPane.showConfirmDialog(frame, panel, "Adicionar Carro", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String placa = placaField.getText().trim();
            String modelo = modeloField.getText().trim();
            
            if (!placa.isEmpty() && !modelo.isEmpty()) {
                carros.add(new Carro(placa, modelo));
                textArea.append("Carro adicionado com sucesso!\n");
            } else {
                JOptionPane.showMessageDialog(frame, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarCliente() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField idField = new JTextField();
        JTextField nomeField = new JTextField();
        
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        
        int result = JOptionPane.showConfirmDialog(frame, panel, "Registrar Cliente", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String nome = nomeField.getText().trim();
            
            if (!id.isEmpty() && !nome.isEmpty()) {
                clientes.add(new Cliente(id, nome));
                textArea.append("Cliente registrado com sucesso!\n");
            } else {
                JOptionPane.showMessageDialog(frame, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alugarCarro() {
        if (carros.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhum carro cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhum cliente registrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lista de carros disponíveis
        List<Carro> disponiveis = new ArrayList<>();
        for (Carro carro : carros) {
            if (carro.getAlugadoPor() == null) {
                disponiveis.add(carro);
            }
        }
        
        if (disponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos os carros já estão alugados.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Criar arrays para o JOptionPane
        String[] carrosArray = new String[disponiveis.size()];
        for (int i = 0; i < disponiveis.size(); i++) {
            carrosArray[i] = disponiveis.get(i).getModelo() + " - " + disponiveis.get(i).getPlaca();
        }
        
        String[] clientesArray = new String[clientes.size()];
        for (int i = 0; i < clientes.size(); i++) {
            clientesArray[i] = clientes.get(i).getNome() + " (" + clientes.get(i).getId() + ")";
        }
        
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JComboBox<String> carroCombo = new JComboBox<>(carrosArray);
        JComboBox<String> clienteCombo = new JComboBox<>(clientesArray);
        
        panel.add(new JLabel("Selecione o carro:"));
        panel.add(carroCombo);
        panel.add(new JLabel("Selecione o cliente:"));
        panel.add(clienteCombo);
        
        int result = JOptionPane.showConfirmDialog(frame, panel, "Alugar Carro", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            int carroIndex = carroCombo.getSelectedIndex();
            int clienteIndex = clienteCombo.getSelectedIndex();
            
            disponiveis.get(carroIndex).alugar(clientes.get(clienteIndex));
            textArea.append("Carro alugado com sucesso para " + clientes.get(clienteIndex).getNome() + "!\n");
        }
    }

    private void devolverCarro() {
        if (carros.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhum carro cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lista de carros alugados
        List<Carro> alugados = new ArrayList<>();
        for (Carro carro : carros) {
            if (carro.getAlugadoPor() != null) {
                alugados.add(carro);
            }
        }
        
        if (alugados.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhum carro está alugado no momento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Criar array para o JOptionPane
        String[] carrosArray = new String[alugados.size()];
        for (int i = 0; i < alugados.size(); i++) {
            carrosArray[i] = alugados.get(i).getModelo() + " - " + alugados.get(i).getPlaca() + 
                            " (Alugado por: " + alugados.get(i).getAlugadoPor().getNome() + ")";
        }
        
        String selectedCarro = (String) JOptionPane.showInputDialog(frame, 
                "Selecione o carro a ser devolvido:", 
                "Devolver Carro", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                carrosArray, 
                carrosArray[0]);
        
        if (selectedCarro != null) {
            for (int i = 0; i < carrosArray.length; i++) {
                if (carrosArray[i].equals(selectedCarro)) {
                    alugados.get(i).devolver();
                    textArea.append("Carro " + alugados.get(i).getModelo() + " devolvido com sucesso!\n");
                    break;
                }
            }
        }
    }

    private void listarCarros() {
        if (carros.isEmpty()) {
            textArea.append("Nenhum carro cadastrado.\n");
        } else {
            textArea.append("--- Lista de Carros ---\n");
            for (Carro carro : carros) {
                textArea.append(carro.toString() + "\n");
            }
            textArea.append("----------------------\n");
        }
    }

    private void gerarRelatorio() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("relatorio.txt"))) {
            pw.println("--- Relatorio de Carros ---");
            if (carros.isEmpty()) {
                pw.println("Nenhum carro cadastrado.");
            } else {
                for (Carro carro : carros) {
                    pw.println(carro);
                }
            }
            textArea.append("Relatorio gerado com sucesso em 'relatorio.txt'.\n");
        } catch (IOException e) {
            textArea.append("Erro ao gerar relatorio: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LocadoraGUI());
    }
}