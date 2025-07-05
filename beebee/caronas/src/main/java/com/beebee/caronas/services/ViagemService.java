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
        Aluno motorista = alunoRepository.findById(dto.getMotoristaId())
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
            .motorista(motorista)
            .build();
    }

    public ViagemDTO save(ViagemDTO dto) {
        Viagem viagem = toEntity(dto);
        viagem = viagemRepository.save(viagem);
        return toDTO(viagem);
    }

    public List<ViagemDTO> getAll() {
        return viagemRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public ViagemDTO getById(Long id) {
        Viagem viagem = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));
        return toDTO(viagem);
    }

    public ViagemDTO update(Long id, ViagemDTO dto) {
        Objects.requireNonNull(id, "Viagem ID cannot be null");

        Viagem viagem = viagemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", id));

        if (dto.getDescricao() != null) {
            viagem.setDescricao(dto.getDescricao());
        }
        if (dto.getSituacao() != null) {
            viagem.setSituacao(dto.getSituacao());
        }

        Viagem updated = viagemRepository.save(viagem);
        return toDTO(updated);
    }

    public void delete(Long id) {
        if (!viagemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Viagem", id);
        }
        viagemRepository.deleteById(id);
    }
}
