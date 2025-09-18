package br.com.medstock;

import br.com.medstock.application.EstoqueService;
import br.com.medstock.domain.repository.MaterialDAO;
import br.com.medstock.infrastructure.config.JpaUtil;
import br.com.medstock.infrastructure.persistence.JpaMaterialRepository;
import br.com.medstock.presentation.EstoqueFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        startApp();
    }

    public static void startApp() {
        try {
            MaterialDAO materialDAO = new JpaMaterialRepository();
            EstoqueService estoqueService = new EstoqueService(materialDAO);

            SwingUtilities.invokeLater(() -> {
                EstoqueFrame frame = new EstoqueFrame(estoqueService);
                frame.setVisible(true);
            });

            Runtime.getRuntime().addShutdownHook(new Thread(JpaUtil::shutdown));

        } catch (Exception e) {
            showErrorAndExit(e);
        }
    }

    static void showErrorAndExit(Exception e) {
        JOptionPane.showMessageDialog(
                null,
                "Falha crítica ao iniciar a aplicação: " + e.getMessage(),
                "Erro Fatal",
                JOptionPane.ERROR_MESSAGE
        );
        e.printStackTrace();
        System.exit(1);
    }
}