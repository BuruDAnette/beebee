package com.beebee.caronas.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beebee.caronas.dto.ViagemAlunoDTO;
import com.beebee.caronas.services.ViagemAlunoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/viagens-alunos")
@RequiredArgsConstructor
@CrossOrigin(origins= "4200")
public class ViagemAlunoController {
    private final ViagemAlunoService viagemAlunoService;

    @GetMapping("/{id}")
    public ResponseEntity<ViagemAlunoDTO> getById(@PathVariable Long id) {
        ViagemAlunoDTO studentTrip = viagemAlunoService.getById(id);
        return ResponseEntity.ok(studentTrip);
    }

    @PostMapping
    public ResponseEntity<ViagemAlunoDTO> save(@Valid @RequestBody ViagemAlunoDTO dto) {
        ViagemAlunoDTO savedStudentTrip = viagemAlunoService.save(dto);
        return ResponseEntity.status(201).body(savedStudentTrip);
    }

    @GetMapping
    public ResponseEntity<List<ViagemAlunoDTO>> getAll() {
        List<ViagemAlunoDTO> studentTrips = viagemAlunoService.getAll();
        return ResponseEntity.ok(studentTrips);
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