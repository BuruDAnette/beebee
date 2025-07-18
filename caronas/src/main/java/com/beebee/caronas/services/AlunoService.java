package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.AlunoDTO;
import com.beebee.caronas.dto.AlunoCadastroDTO;
import com.beebee.caronas.dto.LoginDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Aluno.StatusCadastro;
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

    private Aluno toEntity(AlunoCadastroDTO dto) {
        if (alunoRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BusinessRuleException("CPF já cadastrado");
        }
        if (alunoRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new BusinessRuleException("Login já em uso");
        }
        if (alunoRepository.existsByEmailAndIdNot(dto.getEmail(), -1L)) {
            throw new BusinessRuleException("Email já está em uso");
        }

        return Aluno.builder()
            .nome(dto.getNome())
            .cpf(dto.getCpf())
            .email(dto.getEmail())
            .mediaMotorista(0.0)
            .mediaCaronista(0.0)
            .login(dto.getLogin())
            .senha(dto.getSenha())
            .build();
    }

    public AlunoDTO save(AlunoCadastroDTO dto) { 
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
    
    public AlunoDTO updateStatusCadastro(Long id, StatusCadastro newStatus) {
        Aluno student = alunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
        student.setStatusCadastro(newStatus);
        Aluno updatedStudent = alunoRepository.save(student);
        return toDTO(updatedStudent);
    }

    public AlunoDTO updatePassword(Long id, String newPassword) {
        Aluno student = alunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
        student.setSenha(newPassword);
        Aluno updatedStudent = alunoRepository.save(student);
        return toDTO(updatedStudent);
    }

    public void delete(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno", id);
        }
        alunoRepository.deleteById(id);
    }

    public AlunoDTO autenticar(LoginDTO dto) {
        return alunoRepository.findByLoginAndSenha(dto.getLogin(), dto.getSenha())
            .map(this::toDTO)
            .orElseThrow(() -> new BusinessRuleException("Login ou senha inválidos"));
    }
}