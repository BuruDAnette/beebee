package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.ViagemAlunoDTO;
import com.beebee.caronas.services.ViagemAlunoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagens-alunos")
@RequiredArgsConstructor
public class ViagemAlunoController {
    private final ViagemAlunoService viagemAlunoService;

    @GetMapping("/{id}")
    public ResponseEntity<ViagemAlunoDTO> getById(@PathVariable Long id) {
        ViagemAlunoDTO studentTrip = viagemAlunoService.getById(id);
        return ResponseEntity.ok(studentTrip);
    }

    @GetMapping
    public ResponseEntity<List<ViagemAlunoDTO>> getAll() {
        List<ViagemAlunoDTO> studentTrips = viagemAlunoService.getAll();
        return ResponseEntity.ok(studentTrips);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ViagemAlunoDTO>> getByAlunoId(@PathVariable Long alunoId) {
        List<ViagemAlunoDTO> viagensDoAluno = viagemAlunoService.findByAlunoId(alunoId);
        return ResponseEntity.ok(viagensDoAluno);
    }

    @GetMapping("/viagem/{viagemId}")
    public ResponseEntity<List<ViagemAlunoDTO>> getByViagemId(@PathVariable Long viagemId) {
        List<ViagemAlunoDTO> pedidosDaViagem = viagemAlunoService.findByViagemId(viagemId);
        return ResponseEntity.ok(pedidosDaViagem);
    }

    @PostMapping
    public ResponseEntity<ViagemAlunoDTO> save(@Valid @RequestBody ViagemAlunoDTO dto) {
        ViagemAlunoDTO savedStudentTrip = viagemAlunoService.save(dto);
        return ResponseEntity.status(201).body(savedStudentTrip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemAlunoDTO> update(@PathVariable Long id, @Valid @RequestBody ViagemAlunoDTO dto) {
        dto.setId(id);
        ViagemAlunoDTO updatedStudentTrip = viagemAlunoService.update(id, dto);
        return ResponseEntity.ok(updatedStudentTrip);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viagemAlunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}