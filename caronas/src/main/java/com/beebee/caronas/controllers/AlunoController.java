package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.AlunoDTO;
import com.beebee.caronas.dto.AlunoCadastroDTO;
import com.beebee.caronas.dto.LoginDTO;
import com.beebee.caronas.services.AlunoService;
import com.beebee.caronas.dto.AlterarSenhaDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {
    private final AlunoService alunoService;

    // ALTERADO: Método save agora recebe AlunoCadastroDTO
    @PostMapping
    public ResponseEntity<AlunoDTO> save(@Valid @RequestBody AlunoCadastroDTO alunoCadastroDTO) {
        AlunoDTO savedStudent = alunoService.save(alunoCadastroDTO);
        return ResponseEntity.status(201).body(savedStudent);
    }

    @PostMapping("/autenticar")
    public ResponseEntity<AlunoDTO> autenticar(@RequestBody LoginDTO dto) {
        AlunoDTO aluno = alunoService.autenticar(dto);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/{id}/foto")
    public ResponseEntity<AlunoDTO> uploadFoto(
            @PathVariable Long id, 
            @RequestParam("file") MultipartFile file) {
        AlunoDTO alunoAtualizado = alunoService.updateFoto(id, file);
        return ResponseEntity.ok(alunoAtualizado);
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
    public ResponseEntity<AlunoDTO> update(@PathVariable Long id, @Valid @RequestBody AlunoDTO alunoDTO) {
        alunoDTO.setId(id);
        AlunoDTO updatedStudent = alunoService.update(id, alunoDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody AlterarSenhaDTO dto) {
        alunoService.updatePassword(id, dto.getSenhaAtual(), dto.getNovaSenha());
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}