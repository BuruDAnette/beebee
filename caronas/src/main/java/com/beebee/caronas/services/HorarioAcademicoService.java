package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.HorarioAcademicoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.HorarioAcademico;
import com.beebee.caronas.exceptions.ResourceNotFoundException;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.HorarioAcademicoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HorarioAcademicoService {
    private final HorarioAcademicoRepository horarioRepository;
    private final AlunoRepository alunoRepository;

    private HorarioAcademicoDTO toDTO(HorarioAcademico horario) {
        return HorarioAcademicoDTO.builder()
            .id(horario.getId())
            .descricao(horario.getDescricao())
            .dia(horario.getDia())
            .horario(horario.getHorario())
            .situacao(horario.getSituacao())
            .idAluno(horario.getAluno().getId())
            .build();
    }

    private HorarioAcademico toEntity(HorarioAcademicoDTO dto) {
        Aluno student = alunoRepository.findById(dto.getIdAluno())
            .orElseThrow(() -> new ResourceNotFoundException("Aluno", dto.getIdAluno()));

        return HorarioAcademico.builder()
            .id(dto.getId())
            .descricao(dto.getDescricao())
            .dia(dto.getDia())
            .horario(dto.getHorario())
            .situacao(dto.getSituacao())
            .aluno(student)
            .build();
    }

    public HorarioAcademicoDTO save(HorarioAcademicoDTO dto) {
        HorarioAcademico savedSchedule = horarioRepository.save(toEntity(dto));
        return toDTO(savedSchedule);
    }
    public List<HorarioAcademicoDTO> saveBatch(List<HorarioAcademicoDTO> dtos) {
        List<HorarioAcademico> savedSchedules = dtos.stream()
            .map(this::toEntity)
            .toList();
        return horarioRepository.saveAll(savedSchedules)
            .stream()
            .map(this::toDTO)
            .toList();
    }
    public List<HorarioAcademicoDTO> getAll() {
        return horarioRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public List<HorarioAcademicoDTO> getByStudentId(Long idAluno) {
        if (!alunoRepository.existsById(idAluno)) {
            throw new ResourceNotFoundException("Aluno", idAluno);
        }
        return horarioRepository.findByAlunoId(idAluno)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    public HorarioAcademicoDTO getById(Long id) {
        return horarioRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Horário acadêmico", id));
    }
    public HorarioAcademicoDTO update(Long id, HorarioAcademicoDTO dto) {
        HorarioAcademico schedule = horarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Horário acadêmico", id));

        schedule.setDescricao(dto.getDescricao());
        schedule.setDia(dto.getDia());
        schedule.setHorario(dto.getHorario());
        schedule.setSituacao(dto.getSituacao());

        if (!schedule.getAluno().getId().equals(dto.getIdAluno())) {
            Aluno newStudent = alunoRepository.findById(dto.getIdAluno())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno", dto.getIdAluno()));
            schedule.setAluno(newStudent);
        }

        HorarioAcademico updatedSchedule = horarioRepository.save(schedule);
        return toDTO(updatedSchedule);
    }
    public void delete(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horário acadêmico", id);
        }
        horarioRepository.deleteById(id);
    }
}