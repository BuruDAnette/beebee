package com.beebee.caronas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beebee.caronas.dto.HorarioAcademicoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.HorarioAcademico;
import com.beebee.caronas.repositories.AlunoRepository;
import com.beebee.caronas.repositories.HorarioAcademicoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HorarioAcademicoService {
    private final HorarioAcademicoRepository horarioRepository;
    private final AlunoRepository alunoRepository;

    private HorarioAcademicoDTO converterParaDTO(HorarioAcademico horario) {
        return HorarioAcademicoDTO.builder()
                .id(horario.getId())
                .descricao(horario.getDescricao())
                .dia(horario.getDia())
                .horario(horario.getHorario())
                .situacao(horario.getSituacao())
                .idAluno(horario.getAluno().getId())
                .build();
    }

    private HorarioAcademico converterParaEntidade(HorarioAcademicoDTO dto) {
        Aluno aluno = alunoRepository.findById(dto.getIdAluno())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + dto.getIdAluno()));

        return HorarioAcademico.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .dia(dto.getDia())
                .horario(dto.getHorario())
                .situacao(dto.getSituacao())
                .aluno(aluno)
                .build();
    }

    public HorarioAcademicoDTO salvar(HorarioAcademicoDTO dto) {
        HorarioAcademico horarioSalvo = horarioRepository.save(converterParaEntidade(dto));
        return converterParaDTO(horarioSalvo);
    }

    public List<HorarioAcademicoDTO> salvarEmLote(List<HorarioAcademicoDTO> dtos) {
        List<HorarioAcademico> horarios = dtos.stream()
                .map(this::converterParaEntidade)
                .toList();
        List<HorarioAcademico> horariosSalvos = horarioRepository.saveAll(horarios);
        return horariosSalvos.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<HorarioAcademicoDTO> listarTodos() {
        return horarioRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<HorarioAcademicoDTO> listarPorAluno(Long idAluno) {
        return horarioRepository.findByAlunoId(idAluno).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public HorarioAcademicoDTO buscarPorId(Long id) {
        return horarioRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Horário não encontrado com ID: " + id));
    }

    public void excluir(Long id) {
        if (!horarioRepository.existsById(id)) {
            throw new RuntimeException("Horário não encontrado com ID: " + id);
        }
        horarioRepository.deleteById(id);
    }
}