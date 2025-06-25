package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.VeiculoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Veiculo;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.VeiculoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;
    private final AlunoRepository alunoRepository;

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
            .orElseThrow(() -> new ResourceNotFoundException("Motorista", dto.getMotoristaId()));

        return Veiculo.builder()
            .id(dto.getId())
            .placa(dto.getPlaca())
            .modelo(dto.getModelo())
            .cor(dto.getCor())
            .motorista(motorista)
            .build();
    }

    public VeiculoDTO salvar(VeiculoDTO dto) {
        if (dto.getPlaca() == null || dto.getPlaca().isBlank()) {
            throw new BusinessRuleException("Placa do veículo é obrigatória");
        }
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
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", id));
        return converterParaDTO(veiculo);
    }
    public VeiculoDTO atualizar(Long id, VeiculoDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", id));
        
        if (dto.getModelo() != null) {
            veiculo.setModelo(dto.getModelo());
        }
        if (dto.getCor() != null) {
            veiculo.setCor(dto.getCor());
        }
        
        Veiculo veiculoAtualizado = veiculoRepository.save(veiculo);
        return converterParaDTO(veiculoAtualizado);
    }
    public void excluir(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo", id);
        }
        veiculoRepository.deleteById(id);
    }
}
