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

        if (dto.getNotaCaroneiro() < 1 || dto.getNotaCaroneiro() > 5) {
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

    public AvaliacaoDTO salvar(AvaliacaoDTO dto) {
        Avaliacao avaliacao = toEntity(dto);
        avaliacao = avaliacaoRepository.save(avaliacao);
        return toDTO(avaliacao);
    }
    public List<AvaliacaoDTO> listarTodos() {
        return avaliacaoRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public AvaliacaoDTO buscarPorId(Long id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Avaliação", id));
        return toDTO(avaliacao);
    }
    public void deletar(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação", id);
        }
        avaliacaoRepository.deleteById(id);
    }
}
