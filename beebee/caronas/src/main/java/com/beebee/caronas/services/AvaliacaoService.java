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
            .comentarioCaroneiro(avaliacao.getComentarioCaroneiro())
            .notaCaroneiro(avaliacao.getNotaCaroneiro())
            .comentarioCaronista(avaliacao.getComentarioCaronista())
            .notaCaronista(avaliacao.getNotaCaronista())
            .viagemAlunoId(avaliacao.getViagemAluno().getId())
            .build();
    }

    private Avaliacao toEntity(AvaliacaoDTO dto) {
        ViagemAluno viagemAluno = viagemAlunoRepository.findById(dto.getViagemAlunoId())
            .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", dto.getViagemAlunoId()));

        if (dto.getNotaCaroneiro() != null && (dto.getNotaCaroneiro() < 1 || dto.getNotaCaroneiro() > 5)) {
            throw new BusinessRuleException("A nota deve ser entre 1 e 5");
        }

        if (dto.getNotaCaronista() != null && (dto.getNotaCaronista() < 1 || dto.getNotaCaronista() > 5)) {
            throw new BusinessRuleException("A nota deve ser entre 1 e 5");
        }

        return Avaliacao.builder()
            .id(dto.getId())
            .descricao(dto.getDescricao())
            .data(dto.getData())
            .comentarioCaroneiro(dto.getComentarioCaroneiro())
            .notaCaroneiro(dto.getNotaCaroneiro())
            .comentarioCaronista(dto.getComentarioCaronista())
            .notaCaronista(dto.getNotaCaronista())
            .viagemAluno(viagemAluno)
            .build();
    }

    public AvaliacaoDTO save(AvaliacaoDTO dto) {
        Avaliacao saved = avaliacaoRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    public List<AvaliacaoDTO> getAll() {
        return avaliacaoRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public AvaliacaoDTO getById(Long id) {
        return avaliacaoRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));
    }

    public AvaliacaoDTO update(Long id, AvaliacaoDTO dto) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));

        if (dto.getDescricao() != null) avaliacao.setDescricao(dto.getDescricao());
        if (dto.getComentarioCaroneiro() != null) avaliacao.setComentarioCaroneiro(dto.getComentarioCaroneiro());
        if (dto.getNotaCaroneiro() != null) {
            if (dto.getNotaCaroneiro() < 1 || dto.getNotaCaroneiro() > 5) {
                throw new BusinessRuleException("A nota deve ser entre 1 e 5");
            }
            avaliacao.setNotaCaroneiro(dto.getNotaCaroneiro());
        }
        if (dto.getComentarioCaronista() != null) avaliacao.setComentarioCaronista(dto.getComentarioCaronista());
        if (dto.getNotaCaronista() != null) {
            if (dto.getNotaCaronista() < 1 || dto.getNotaCaronista() > 5) {
                throw new BusinessRuleException("A nota deve ser entre 1 e 5");
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
