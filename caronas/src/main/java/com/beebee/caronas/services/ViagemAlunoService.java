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
import com.beebee.caronas.exceptions.BusinessRuleException;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
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

    private ViagemAlunoDTO toDTO(ViagemAluno viagemAluno) {
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

    private ViagemAluno toEntity(ViagemAlunoDTO dto) {
        Aluno student = alunoRepository.findById(dto.getAlunoId())
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", dto.getAlunoId()));
        
        Viagem trip = viagemRepository.findById(dto.getViagemId())
            .orElseThrow(() -> new ResourceNotFoundException("Viagem", dto.getViagemId()));

        if (dto.getSituacao() == Situacao.CONFIRMADA && dto.getDataConfirmacao() == null) {
            throw new BusinessRuleException("Data de confirmação é obrigatória");
        }

        return ViagemAluno.builder()
            .id(dto.getId())
            .dataSolicitacao(LocalDateTime.now())
            .dataConfirmacao(dto.getDataConfirmacao())
            .observacao(dto.getObservacao())
            .situacao(dto.getSituacao())
            .aluno(student)
            .viagem(trip)
            .build();
    }

    public ViagemAlunoDTO save(ViagemAlunoDTO dto) {
        ViagemAluno savedStudentTrip = toEntity(dto);
        savedStudentTrip = viagemAlunoRepository.save(savedStudentTrip);
        return toDTO(savedStudentTrip);
    }
    public List<ViagemAlunoDTO> getAll() {
        return viagemAlunoRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public ViagemAlunoDTO getById(Long id) {
        ViagemAluno studentTrip = viagemAlunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", id));
        return toDTO(studentTrip);
    }

    public ViagemAlunoDTO update(Long id, ViagemAlunoDTO dto) {
        ViagemAluno studentTrip = viagemAlunoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ViagemAluno", id));

        if (dto.getObservacao() != null) {
            studentTrip.setObservacao(dto.getObservacao());
        }
        if (dto.getSituacao() != null) {
            studentTrip.setSituacao(dto.getSituacao());
            if (dto.getSituacao() == Situacao.CONFIRMADA) {
                studentTrip.setDataConfirmacao(LocalDateTime.now());
            }
        }

        ViagemAluno updatedStudentTrip = viagemAlunoRepository.save(studentTrip);
        return toDTO(updatedStudentTrip);
    }
    public void delete(Long id) {
        if (!viagemAlunoRepository.existsById(id)) {
            throw new ResourceNotFoundException("ViagemAluno", id);
        }
        viagemAlunoRepository.deleteById(id);
    }
}