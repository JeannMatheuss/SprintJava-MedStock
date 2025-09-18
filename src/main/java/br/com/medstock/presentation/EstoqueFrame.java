package br.com.medstock.presentation;

import br.com.medstock.application.EstoqueService;
import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.NivelEstoqueStatus;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class EstoqueFrame extends JFrame {

    private final EstoqueService estoqueService;

    private JLabel quantidadeLabel;
    private JLabel statusLabel;

    public EstoqueFrame(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;

        setTitle("Controle de Estoque - AstraZeneca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 250);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        consultarEstoqueInicialComWorker();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 5));

        JLabel labTitle = new JLabel("Laboratório Pfizer", SwingConstants.CENTER);
        labTitle.setFont(new Font("Arial", Font.BOLD, 20));

        quantidadeLabel = new JLabel("Quantidade: --", SwingConstants.CENTER);
        quantidadeLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        statusLabel = new JLabel("Status: Carregando...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setOpaque(true);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBackground(Color.GRAY);

        infoPanel.add(labTitle);
        infoPanel.add(quantidadeLabel);
        infoPanel.add(statusLabel);

        return infoPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton adicionarBtn = new JButton("Adicionar (+10)");
        adicionarBtn.addActionListener(e -> adicionarEstoque());

        JButton removerBtn = new JButton("Remover (-10)");
        removerBtn.addActionListener(e -> removerEstoque());

        buttonPanel.add(adicionarBtn);
        buttonPanel.add(removerBtn);

        return buttonPanel;
    }

    private void consultarEstoqueInicialComWorker() {
        SwingWorker<Material, Void> worker = new SwingWorker<>() {
            @Override
            protected Material doInBackground() throws Exception {
                return estoqueService.consultarEstoque();
            }

            @Override
            protected void done() {
                try {
                    Material material = get();
                    atualizarTela(material);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(EstoqueFrame.this, "Erro ao carregar dados iniciais: " + e.getCause().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        };
        worker.execute();
    }

    private void adicionarEstoque() {
        try {
            Material material = estoqueService.adicionarEstoque();
            atualizarTela(material);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Operação falhou: " + e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removerEstoque() {
        try {
            Material material = estoqueService.removerEstoque();
            atualizarTela(material);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Operação falhou: " + e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void atualizarTela(Material material) {
        quantidadeLabel.setText("Quantidade: " + material.getQuantidadeDisponivel());
        NivelEstoqueStatus status = NivelEstoqueStatus.valueOf(material.getStatus());
        statusLabel.setText("Status: " + status.getTexto());
        statusLabel.setBackground(status.getCor());
    }
}