package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.ViagemAlunoDTO;
import com.beebee.caronas.services.ViagemAlunoService;
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

    @PostMapping
    public ResponseEntity<ViagemAlunoDTO> save(@RequestBody ViagemAlunoDTO dto) {
        ViagemAlunoDTO savedStudentTrip = viagemAlunoService.save(dto);
        return ResponseEntity.status(201).body(savedStudentTrip);
    }

    @GetMapping
    public ResponseEntity<List<ViagemAlunoDTO>> getAll() {
        List<ViagemAlunoDTO> studentTrips = viagemAlunoService.getAll();
        return ResponseEntity.ok(studentTrips);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemAlunoDTO> update(@PathVariable Long id, @RequestBody ViagemAlunoDTO dto) {
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