package br.com.medstock;

import br.com.medstock.application.EstoqueService;
import br.com.medstock.domain.repository.FuncionarioRepository;
import br.com.medstock.domain.repository.MaterialRepository;
import br.com.medstock.domain.repository.RegistroDeConsumoRepository;
import br.com.medstock.domain.repository.UnidadeRepository;
import br.com.medstock.infrastructure.config.JpaUtil;
import br.com.medstock.infrastructure.persistence.JpaFuncionarioRepository;
import br.com.medstock.infrastructure.persistence.JpaMaterialRepository;
import br.com.medstock.infrastructure.persistence.JpaRegistroDeConsumoRepository;
import br.com.medstock.infrastructure.persistence.JpaUnidadeRepository;

public class Main {
    public static void main(String[] args) {
        try {
            MaterialRepository materialRepository = new JpaMaterialRepository();
            UnidadeRepository unidadeRepository = new JpaUnidadeRepository();
            RegistroDeConsumoRepository registroRepo = new JpaRegistroDeConsumoRepository();
            FuncionarioRepository funcionarioRepository = new JpaFuncionarioRepository();

            EstoqueService estoqueService = new EstoqueService(
                    materialRepository,
                    unidadeRepository,
                    registroRepo,
                    funcionarioRepository
            );

            System.out.println("\n>>> Listando materiais com estoque inicial:");
            materialRepository.findAll().forEach(m ->
                    System.out.printf(" - %s: %d unidades%n", m.getNome(), m.getQuantidadeDisponivel())
            );

            System.out.println("\n>>> Registrando consumo de 10 unidades de 'Reagente Alfa' (ID 1)...");
            estoqueService.registrarConsumo(1, 1, 1, 10);
            System.out.println("Consumo registrado com sucesso!");

            System.out.println("\n>>> Verificando novo estoque de 'Reagente Alfa':");
            materialRepository.findById(1).ifPresent(m ->
                    System.out.printf(" - %s: %d unidades%n", m.getNome(), m.getQuantidadeDisponivel())
            );

            System.out.println("\n>>> Listando materiais com estoque baixo (< 500):");
            estoqueService.listarMateriaisComEstoqueBaixo(500).forEach(m ->
                    System.out.printf(" - %s: %d unidades%n", m.getNome(), m.getQuantidadeDisponivel())
            );

        } catch (Exception e) {
            System.err.println("\nOcorreu um erro na aplicação:");
            e.printStackTrace();
        } finally {
            JpaUtil.shutdown();
            System.out.println("\nAplicação finalizada.");
        }
    }
}