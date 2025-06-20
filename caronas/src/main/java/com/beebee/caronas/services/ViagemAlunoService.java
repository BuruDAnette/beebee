package com.beebee.caronas.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.ViagemAlunoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Viagem;
import com.beebee.caronas.entities.ViagemAluno;
import com.beebee.caronas.entities.ViagemAluno.Situacao;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.ViagemRepository;
import com.beebee.caronas.repositories.ViagemAlunoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViagemAlunoService {
    private final ViagemAlunoRepository viagemAlunoRepository;
    private final AlunoRepository alunoRepository;
    private final ViagemRepository viagemRepository;

    private ViagemAlunoDTO converterParaDTO(ViagemAluno viagemAluno) {
        return ViagemAlunoDTO.builder()
                .id(viagemAluno.getId())
                .dataSolicitacao(viagemAluno.getDataSolicitacao())
                .dataConfirmacao(viagemAluno.getDataConfirmacao())
                .observacao(viagemAluno.getObservacao())
                .situacao(viagemAluno.getSituacao())
                .alunoId(viagemAluno.getAluno().getId())
                .viagemId(viagemAluno.getViagem().getId())
                .build();
    }

    private ViagemAluno converterParaEntidade(ViagemAlunoDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + dto.getAlunoId()));
        Viagem viagem = viagemRepository.findById(dto.getViagemId())
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada com ID: " + dto.getViagemId()));

        return ViagemAluno.builder()
                .id(dto.getId())
                .dataSolicitacao(dto.getDataSolicitacao())
                .dataConfirmacao(dto.getDataConfirmacao())
                .observacao(dto.getObservacao())
                .situacao(dto.getSituacao())
                .aluno(aluno)
                .viagem(viagem)
                .build();
    }

    public ViagemAlunoDTO salvar(ViagemAlunoDTO dto) {
        ViagemAluno viagemAluno = converterParaEntidade(dto);
        viagemAluno = viagemAlunoRepository.save(viagemAluno);
        return converterParaDTO(viagemAluno);
    }

    public List<ViagemAlunoDTO> listarTodos() {
        return viagemAlunoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public ViagemAlunoDTO buscarPorId(Long id) {
        ViagemAluno viagemAluno = viagemAlunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ViagemAluno não encontrada com o ID: " + id));
        return converterParaDTO(viagemAluno);
    }

    public void excluir(Long id) {
        if (!viagemAlunoRepository.existsById(id)) {
            throw new RuntimeException("ViagemAluno não encontrada com o ID: " + id);
        }
        viagemAlunoRepository.deleteById(id);
    }

    public ViagemAlunoDTO atualizarStatus(Long id, Situacao novoSituacao) {
        ViagemAluno viagemAluno = viagemAlunoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
        
        if (novoSituacao == Situacao.CONFIRMADA) {
            viagemAluno.setDataConfirmacao(LocalDateTime.now());
        }
        
        viagemAluno.setSituacao(novoSituacao);
        return converterParaDTO(viagemAlunoRepository.save(viagemAluno));
    }
}