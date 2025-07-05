package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.AvaliacaoDTO;
import com.beebee.caronas.entities.Avaliacao;
import com.beebee.caronas.entities.ViagemAluno;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AvaliacaoRepository;
import com.beebee.caronas.repositories.ViagemAlunoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ViagemAlunoRepository viagemAlunoRepository;

    private AvaliacaoDTO toDTO(Avaliacao avaliacao) {
        return AvaliacaoDTO.builder()
            .id(avaliacao.getId())
            .descricao(avaliacao.getDescricao())
            .data(avaliacao.getData())
            .comentarioMotorista(avaliacao.getComentarioMotorista())
            .notaMotorista(avaliacao.getNotaMotorista())
            .comentarioCaronista(avaliacao.getComentarioCaronista())
            .notaCaronista(avaliacao.getNotaCaronista())
            .viagemAlunoId(avaliacao.getViagemAluno().getId())
            .build();
    }

    private Avaliacao toEntity(AvaliacaoDTO dto) {
        ViagemAluno studentTrip = viagemAlunoRepository.findById(dto.getViagemAlunoId())
            .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", dto.getViagemAlunoId()));

        if (dto.getNotaMotorista() != null && (dto.getNotaMotorista() < 1 || dto.getNotaMotorista() > 5)) {
            throw new BusinessRuleException("Driver rating must be between 1 and 5");
        }
        if (dto.getNotaCaronista() != null && (dto.getNotaCaronista() < 1 || dto.getNotaCaronista() > 5)) {
            throw new BusinessRuleException("Passenger rating must be between 1 and 5");
        }

        return Avaliacao.builder()
            .id(dto.getId())
            .descricao(dto.getDescricao())
            .data(dto.getData())
            .comentarioMotorista(dto.getComentarioMotorista())
            .notaMotorista(dto.getNotaMotorista())
            .comentarioCaronista(dto.getComentarioCaronista())
            .notaCaronista(dto.getNotaCaronista())
            .viagemAluno(studentTrip)
            .build();
    }

    public AvaliacaoDTO save(AvaliacaoDTO dto) {
        Avaliacao avaliacao = toEntity(dto);
        avaliacao = avaliacaoRepository.save(avaliacao);
        return toDTO(avaliacao);
    }

    public List<AvaliacaoDTO> getAll() {
        return avaliacaoRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public AvaliacaoDTO getById(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));
        return toDTO(avaliacao);
    }

    public AvaliacaoDTO update(Long id, AvaliacaoDTO dto) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));

        if (dto.getDescricao() != null) {
            avaliacao.setDescricao(dto.getDescricao());
        }
        if (dto.getComentarioMotorista() != null) {
            avaliacao.setComentarioMotorista(dto.getComentarioMotorista());
        }
        if (dto.getNotaMotorista() != null) {
            if (dto.getNotaMotorista() < 1 || dto.getNotaMotorista() > 5) {
                throw new BusinessRuleException("Driver rating must be between 1 and 5");
            }
            avaliacao.setNotaMotorista(dto.getNotaMotorista());
        }
        if (dto.getComentarioCaronista() != null) {
            avaliacao.setComentarioCaronista(dto.getComentarioCaronista());
        }
        if (dto.getNotaCaronista() != null) {
            if (dto.getNotaCaronista() < 1 || dto.getNotaCaronista() > 5) {
                throw new BusinessRuleException("Passenger rating must be between 1 and 5");
            }
            avaliacao.setNotaCaronista(dto.getNotaCaronista());
        }

        Avaliacao updated = avaliacaoRepository.save(avaliacao);
        return toDTO(updated);
    }

    public void delete(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação", id);
        }
        avaliacaoRepository.deleteById(id);
    }
}
