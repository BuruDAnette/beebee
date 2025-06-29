package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.HorarioAcademicoDTO;
import com.beebee.caronas.services.HorarioAcademicoService;

import jakarta.validation.Valid;
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
    public ResponseEntity<HorarioAcademicoDTO> save(@Valid @RequestBody HorarioAcademicoDTO dto) {
        HorarioAcademicoDTO savedSchedule  = horarioService.save(dto);
        return ResponseEntity.status(201).body(savedSchedule);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<HorarioAcademicoDTO>> saveBatch(@Valid @RequestBody List<HorarioAcademicoDTO> dtos) {
        List<HorarioAcademicoDTO> savedSchedules = horarioService.saveBatch(dtos);
        return ResponseEntity.ok(savedSchedules);
    }

    @GetMapping
    public ResponseEntity<List<HorarioAcademicoDTO>> getAll() {
        List<HorarioAcademicoDTO> schedules = horarioService.getAll();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/aluno/{idAluno}")
    public ResponseEntity<List<HorarioAcademicoDTO>> getByStudentId(@PathVariable Long idAluno) {
        List<HorarioAcademicoDTO> schedules = horarioService.getByStudentId(idAluno);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioAcademicoDTO> getById(@PathVariable Long id) {
        HorarioAcademicoDTO schedule = horarioService.getById(id);
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioAcademicoDTO> update(@PathVariable Long id, @Valid @RequestBody HorarioAcademicoDTO dto) {
        dto.setId(id); 
        HorarioAcademicoDTO updatedSchedule = horarioService.update(id, dto);
        return ResponseEntity.ok(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        horarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}