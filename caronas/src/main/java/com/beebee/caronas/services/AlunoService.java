package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.AlunoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.repositories.AlunoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoService {
    @Autowired
    private final AlunoRepository alunoRepository;

    private AlunoDTO converterParaDTO(Aluno aluno) {
        return AlunoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .cpf(aluno.getCpf())
                .email(aluno.getEmail())
                .mediaCaroneiro(aluno.getMediaCaroneiro())
                .mediaCaronista(aluno.getMediaCaronista())
                .login(aluno.getLogin())
                .build();
    }

    private Aluno converterParaEntidade(AlunoDTO dto) {
        return Aluno.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .mediaCaroneiro(dto.getMediaCaroneiro())
                .mediaCaronista(dto.getMediaCaronista())
                .login(dto.getLogin())
                .senha("default")
                .build();
    }

    public AlunoDTO salvar(AlunoDTO dto) {
        Aluno alunoSalvo = alunoRepository.save(converterParaEntidade(dto));
        return converterParaDTO(alunoSalvo);
    }
    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    public AlunoDTO buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + id));
    }
    public void excluir(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado com o ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
}
