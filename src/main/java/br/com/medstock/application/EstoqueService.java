package br.com.medstock.application;

import br.com.medstock.domain.exception.RecursoNaoEncontradoException;
import br.com.medstock.domain.model.Funcionario;
import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.model.RegistroDeConsumo;
import br.com.medstock.domain.model.Unidade;
import br.com.medstock.domain.repository.FuncionarioRepository;
import br.com.medstock.domain.repository.MaterialRepository;
import br.com.medstock.domain.repository.RegistroDeConsumoRepository;
import br.com.medstock.domain.repository.UnidadeRepository;

import java.util.List;

public class EstoqueService {

    private final MaterialRepository materialRepository;
    private final UnidadeRepository unidadeRepository;
    private final RegistroDeConsumoRepository registroDeConsumoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public EstoqueService(
            MaterialRepository materialRepository,
            UnidadeRepository unidadeRepository,
            RegistroDeConsumoRepository registroDeConsumoRepository,
            FuncionarioRepository funcionarioRepository) {
        this.materialRepository = materialRepository;
        this.unidadeRepository = unidadeRepository;
        this.registroDeConsumoRepository = registroDeConsumoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public RegistroDeConsumo registrarConsumo(int materialId, int unidadeId, int funcionarioId, int quantidade) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Material com ID " + materialId + " não encontrado."));

        Unidade unidade = unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade com ID " + unidadeId + " não encontrada."));

        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário com ID " + funcionarioId + " não encontrado."));

        material.consumir(quantidade);

        RegistroDeConsumo registro = new RegistroDeConsumo(material, unidade, quantidade, funcionario);

        materialRepository.save(material);
        return registroDeConsumoRepository.save(registro);
    }

    public List<Material> listarMateriaisComEstoqueBaixo(int limite) {
        return materialRepository.findByEstoqueAbaixoDe(limite);
    }
}