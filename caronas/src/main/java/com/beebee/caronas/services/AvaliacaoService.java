package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.AvaliacaoDTO;
import com.beebee.caronas.entities.Avaliacao;
import com.beebee.caronas.entities.ViagemAluno;
import com.beebee.caronas.repositories.AvaliacaoRepository;
import com.beebee.caronas.repositories.ViagemAlunoRepository;

@Service
public class AvaliacaoService {
        @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ViagemAlunoRepository viagemAlunoRepository;

    private Avaliacao toEntity(AvaliacaoDTO dto) {
        ViagemAluno viagemAluno = viagemAlunoRepository.findById(dto.getViagemAlunoId())
                .orElseThrow(() -> new RuntimeException("ViagemAluno não encontrada"));

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
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        return toDTO(avaliacao);
    }

    public void deletar(Long id) {
        avaliacaoRepository.deleteById(id);
    }
}
