package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.VeiculoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Veiculo;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.VeiculoRepository;

@Service

public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private AlunoRepository alunoRepository;


    private VeiculoDTO converterParaDTO(Veiculo veiculo) {
        return VeiculoDTO.builder()
                .id(veiculo.getId())
                .placa(veiculo.getPlaca())
                .modelo(veiculo.getModelo())
                .cor(veiculo.getCor())
                .motoristaId(veiculo.getMotorista().getId())
                .build();
    }

    private Veiculo converterParaEntidade(VeiculoDTO dto) {
        Aluno motorista = alunoRepository.findById(dto.getMotoristaId())
            .orElseThrow(() -> new RuntimeException("Motorista não encontrado"));
        return Veiculo.builder()
            .id(dto.getId())
            .placa(dto.getPlaca())
            .modelo(dto.getModelo())
            .cor(dto.getCor())
            .motorista(motorista)
            .build();
    }

    public VeiculoDTO salvar(VeiculoDTO dto) {
        Veiculo veiculo = converterParaEntidade(dto);
        veiculo = veiculoRepository.save(veiculo);
        return converterParaDTO(veiculo);
    }
    public List<VeiculoDTO> listarTodos() {
        return veiculoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    public VeiculoDTO buscarPorId(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + id));
        return converterParaDTO(veiculo);
    }
    public void excluir(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado com o ID: " + id);
        }
        veiculoRepository.deleteById(id);
    }
}
