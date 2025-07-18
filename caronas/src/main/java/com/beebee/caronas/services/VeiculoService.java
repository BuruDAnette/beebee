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

    private VeiculoDTO toDTO(Veiculo veiculo) {
        return VeiculoDTO.builder()
            .id(veiculo.getId())
            .placa(veiculo.getPlaca())
            .modelo(veiculo.getModelo())
            .cor(veiculo.getCor())
            .motoristaId(veiculo.getMotorista().getId())
            .build();
    }

    private Veiculo toEntity(VeiculoDTO dto) {
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

    public VeiculoDTO save(VeiculoDTO dto) {
        if (dto.getPlaca() == null || dto.getPlaca().isBlank()) {
            throw new BusinessRuleException("Placa do veículo é obrigatória");
        }
        Veiculo savedVehicle = toEntity(dto);
        savedVehicle = veiculoRepository.save(savedVehicle);
        return toDTO(savedVehicle);
    }
    public List<VeiculoDTO> getAll() {
        return veiculoRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public VeiculoDTO getById(Long id) {
        Veiculo vehicle = veiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", id));
        return toDTO(vehicle);
    }

    public List<VeiculoDTO> getByMotoristaId(Long motoristaId) {
        return veiculoRepository.findByMotoristaId(motoristaId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public VeiculoDTO update(Long id, VeiculoDTO dto) {
        Veiculo vehicle = veiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veículo", id));

        if (dto.getModelo() != null) {
            vehicle.setModelo(dto.getModelo());
        }
        if (dto.getCor() != null) {
            vehicle.setCor(dto.getCor());
        }

        Veiculo updatedVehicle = veiculoRepository.save(vehicle);
        return toDTO(updatedVehicle);
    }
    public void delete(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veículo", id);
        }
        veiculoRepository.deleteById(id);
    }
}
