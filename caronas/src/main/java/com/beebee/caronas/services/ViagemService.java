package com.beebee.caronas.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Viagem;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemService {
    private final ViagemRepository viagemRepository;
    private final AlunoRepository alunoRepository;

    private ViagemDTO toDTO(Viagem viagem) {
        return ViagemDTO.builder()
            .id(viagem.getId())
            .descricao(viagem.getDescricao())
            .dataInicio(viagem.getDataInicio())
            .dataFim(viagem.getDataFim())
            .origem(viagem.getOrigem())
            .destino(viagem.getDestino())
            .situacao(viagem.getSituacao())
            .motoristaId(viagem.getMotorista().getId())
            .build();
    }

    private Viagem toEntity(ViagemDTO dto) {
        Aluno driver = alunoRepository.findById(dto.getMotoristaId())
            .orElseThrow(() -> new ResourceNotFoundException("Motorista", dto.getMotoristaId()));

        if (dto.getDataInicio().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Data de início não pode ser no passado");
        }
        
        return Viagem.builder()
            .id(dto.getId())
            .descricao(dto.getDescricao())
            .dataInicio(dto.getDataInicio())
            .dataFim(dto.getDataFim())
            .origem(dto.getOrigem())
            .destino(dto.getDestino())
            .situacao(dto.getSituacao())
            .motorista(driver)
            .build();
    }

    public ViagemDTO save(ViagemDTO dto) {
        Viagem savedTrip = toEntity(dto);
        savedTrip = viagemRepository.save(savedTrip);
        return toDTO(savedTrip);
    }
    public List<ViagemDTO> getAll() {
        return viagemRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public ViagemDTO getById(Long id) {
        Viagem trip = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));
        return toDTO(trip);
    }
    public ViagemDTO update(Long id, ViagemDTO dto) {
        Objects.requireNonNull(id, "ID da viagem não pode ser nulo");

        Viagem trip = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));

        if (dto.getDescricao() != null) {
            trip.setDescricao(dto.getDescricao());
        }
        if (dto.getSituacao() != null) {
            trip.setSituacao(dto.getSituacao());
        }

        Viagem updatedTrip = viagemRepository.save(trip);
        return toDTO(updatedTrip);
    }
    public void delete(Long id) {
        if (!viagemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem", id);
        }
        viagemRepository.deleteById(id);
    }
}
