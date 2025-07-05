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

    private AlunoDTO toDTO(Aluno aluno) {
        return AlunoDTO.builder()
            .id(aluno.getId())
            .nome(aluno.getNome())
            .cpf(aluno.getCpf())
            .email(aluno.getEmail())
            .mediaMotorista(aluno.getMediaMotorista())
            .mediaCaronista(aluno.getMediaCaronista())
            .login(aluno.getLogin())
            .build();
    }

    private Aluno toEntity(AlunoDTO dto) {
        if (dto.getCpf() == null || !dto.getCpf().matches("\\d{11}")) {
            throw new BusinessRuleException("CPF inválido");
        }
        
        return Aluno.builder()
            .id(dto.getId())
            .nome(dto.getNome())
            .cpf(dto.getCpf())
            .email(dto.getEmail())
            .mediaMotorista(dto.getMediaMotorista())
            .mediaCaronista(dto.getMediaCaronista())
            .login(dto.getLogin())
            .senha("default")
            .build();
    }

    public AlunoDTO save(AlunoDTO dto) {
        Aluno savedStudent = toEntity(dto);
        savedStudent = alunoRepository.save(savedStudent);
        return toDTO(savedStudent);
    }
    public List<AlunoDTO> getAll() {
        return alunoRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public AlunoDTO getById(Long id) {
        return alunoRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
    }
    public AlunoDTO update(Long id, AlunoDTO dto) {
        Aluno student = alunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));

        if (dto.getNome() != null) {
            student.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            if (alunoRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new BusinessRuleException("Email já está em uso por outro aluno");
            }
            student.setEmail(dto.getEmail());
        }

        Aluno updatedStudent = alunoRepository.save(student);
        return toDTO(updatedStudent);
    }
    public void delete(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno", id);
        }
        alunoRepository.deleteById(id);
    }
}
