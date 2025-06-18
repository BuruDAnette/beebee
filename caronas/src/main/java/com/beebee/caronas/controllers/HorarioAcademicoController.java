package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.HorarioAcademicoDTO;
import com.beebee.caronas.services.HorarioAcademicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioAcademicoController {
    private final HorarioAcademicoService horarioService;

    @PostMapping
    public ResponseEntity<HorarioAcademicoDTO> salvar(@RequestBody HorarioAcademicoDTO dto) {
        HorarioAcademicoDTO horarioSalvo = horarioService.salvar(dto);
        return ResponseEntity.ok(horarioSalvo);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<HorarioAcademicoDTO>> salvarEmLote(@RequestBody List<HorarioAcademicoDTO> dtos) {
        List<HorarioAcademicoDTO> horariosSalvos = horarioService.salvarEmLote(dtos);
        return ResponseEntity.ok(horariosSalvos);
    }

    @GetMapping
    public ResponseEntity<List<HorarioAcademicoDTO>> listarTodos() {
        List<HorarioAcademicoDTO> horarios = horarioService.listarTodos();
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<List<HorarioAcademicoDTO>> listarPorAluno(@PathVariable Long idAluno) {
        List<HorarioAcademicoDTO> horarios = horarioService.listarPorAluno(idAluno);
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioAcademicoDTO> buscarPorId(@PathVariable Long id) {
        HorarioAcademicoDTO horario = horarioService.buscarPorId(id);
        return ResponseEntity.ok(horario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        horarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}