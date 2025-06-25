package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.AlunoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoService {
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
        if (dto.getCpf() == null || !dto.getCpf().matches("\\d{11}")) {
            throw new BusinessRuleException("CPF inválido");
        }
        
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
        Aluno aluno = converterParaEntidade(dto);
        aluno = alunoRepository.save(aluno);
        return converterParaDTO(aluno);
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
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
    }
    public AlunoDTO atualizar(Long id, AlunoDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
        
        if (dto.getNome() != null) {
            aluno.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            if (alunoRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new BusinessRuleException("Email já está em uso por outro aluno");
            }
            aluno.setEmail(dto.getEmail());
        }
        
        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return converterParaDTO(alunoAtualizado);
    }
    public void excluir(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno", id);
        }
        alunoRepository.deleteById(id);
    }
}
