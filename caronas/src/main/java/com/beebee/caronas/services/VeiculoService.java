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

    private Veiculo toEntity(VeiculoDTO dto) {
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

    private VeiculoDTO toDTO(Veiculo veiculo) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setPlaca(veiculo.getPlaca());
        dto.setModelo(veiculo.getModelo());
        dto.setCor(veiculo.getCor());
        dto.setMotoristaId(veiculo.getMotorista().getId());
        return dto;
    }

    public VeiculoDTO salvar(VeiculoDTO dto) {
        Veiculo veiculo = toEntity(dto);
        veiculo = veiculoRepository.save(veiculo);
        return toDTO(veiculo);
    }

    public List<VeiculoDTO> listarTodos() {
        return veiculoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public VeiculoDTO buscarPorId(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        return toDTO(veiculo);
    }

    public void deletar(Long id) {
        veiculoRepository.deleteById(id);
    }
}
