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

        if (dto.getNotaMotorista() < 1 || dto.getNotaMotorista() > 5) {
            throw new BusinessRuleException("A nota deve ser entre 1 e 5");
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
        Avaliacao savedRating = toEntity(dto);
        savedRating = avaliacaoRepository.save(savedRating);
        return toDTO(savedRating);
    }
    public List<AvaliacaoDTO> getAll() {
        return avaliacaoRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public AvaliacaoDTO getById(Long id) {
        Avaliacao rating = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));
        return toDTO(rating);
    }
    public AvaliacaoDTO update(Long id, AvaliacaoDTO dto) {
        dto.setId(id);
        Avaliacao rating = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));

        if (dto.getDescricao() != null) {
            rating.setDescricao(dto.getDescricao());
        }
        if (dto.getComentarioMotorista() != null) {
            rating.setComentarioMotorista(dto.getComentarioMotorista());
        }
        if (dto.getNotaMotorista() != null) {
            if (dto.getNotaMotorista() < 1 || dto.getNotaMotorista() > 5) {
                throw new BusinessRuleException("A nota deve ser entre 1 e 5");
            }
            rating.setNotaMotorista(dto.getNotaMotorista());
        }
        if (dto.getComentarioCaronista() != null) {
            rating.setComentarioCaronista(dto.getComentarioCaronista());
        }
        if (dto.getNotaCaronista() != null) {
            if (dto.getNotaCaronista() < 1 || dto.getNotaCaronista() > 5) {
                throw new BusinessRuleException("A nota deve ser entre 1 e 5");
            }
            rating.setNotaCaronista(dto.getNotaCaronista());
        }

        Avaliacao updatedRating = avaliacaoRepository.save(rating);
        return toDTO(updatedRating);
    }
    public void delete(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação", id);
        }
        avaliacaoRepository.deleteById(id);
    }
}
