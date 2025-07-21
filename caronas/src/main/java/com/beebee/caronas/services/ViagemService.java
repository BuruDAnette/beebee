package com.beebee.caronas.services;

import java.time.LocalDateTime;
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
import com.beebee.caronas.repositories.VeiculoRepository;
import com.beebee.caronas.repositories.ViagemAlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemService {
    private final ViagemRepository viagemRepository;
    private final AlunoRepository alunoRepository;
    private final VeiculoRepository veiculoRepository; 
    private final ViagemAlunoRepository viagemAlunoRepository;
    
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
            .motoristaNome(viagem.getMotorista().getNome())
            .build();
    }

    private Viagem toEntity(ViagemDTO dto) {
        Aluno driver = alunoRepository.findById(dto.getMotoristaId())
            .orElseThrow(() -> new ResourceNotFoundException("Motorista", dto.getMotoristaId()));

        if (dto.getDataInicio().isBefore(LocalDateTime.now())) {
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
        boolean motoristaTemVeiculo = !veiculoRepository.findByMotoristaId(dto.getMotoristaId()).isEmpty();
        if (!motoristaTemVeiculo) {
            throw new BusinessRuleException("Para criar uma viagem, é necessário ter pelo menos um veículo cadastrado.");
        }

        Viagem savedTrip = toEntity(dto);
        savedTrip = viagemRepository.save(savedTrip);
        return toDTO(savedTrip);
    }
    public List<ViagemDTO> getAll() {
        return viagemRepository.findAll()
            .stream()
            .filter(viagem -> "PLANEJADA".equals(viagem.getSituacao()))
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public List<ViagemDTO> getByMotoristaId(Long motoristaId) {
        return viagemRepository.findByMotoristaId(motoristaId)
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

        boolean hasRequests = !viagemAlunoRepository.findByViagemId(id).isEmpty();

        if (hasRequests) {
            if (dto.getDescricao() != null) {
                trip.setDescricao(dto.getDescricao());
            }
            if (dto.getSituacao() != null) {
                trip.setSituacao(dto.getSituacao());
            }
            if (dto.getOrigem() != null || dto.getDestino() != null || dto.getDataInicio() != null) {
                throw new BusinessRuleException("Não é possível alterar os detalhes da viagem pois ela já possui solicitações.");
            }
        } else {
            if (dto.getDescricao() != null) trip.setDescricao(dto.getDescricao());
            if (dto.getSituacao() != null) trip.setSituacao(dto.getSituacao());
            if (dto.getOrigem() != null) trip.setOrigem(dto.getOrigem());
            if (dto.getDestino() != null) trip.setDestino(dto.getDestino());
            if (dto.getDataInicio() != null) trip.setDataInicio(dto.getDataInicio());
            if (dto.getDataFim() != null) trip.setDataFim(dto.getDataFim());
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