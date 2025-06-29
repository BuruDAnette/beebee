package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.AlunoDTO;
import com.beebee.caronas.services.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
    private final AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoDTO> save(@RequestBody AlunoDTO alunoDTO) {
        AlunoDTO savedStudent = alunoService.save(alunoDTO);
        return ResponseEntity.status(201).body(savedStudent);
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> getAll() {
        List<AlunoDTO> students = alunoService.getAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> getById(@PathVariable Long id) {
        AlunoDTO student = alunoService.getById(id);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> update(@PathVariable Long id, @RequestBody AlunoDTO alunoDTO) {
        alunoDTO.setId(id);
        AlunoDTO updatedStudent = alunoService.update(id, alunoDTO);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
