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
    public ResponseEntity<AlunoDTO> salvar(@RequestBody AlunoDTO alunoDTO) {
        AlunoDTO alunoSalvo = alunoService.salvar(alunoDTO);
        return ResponseEntity.ok(alunoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        List<AlunoDTO> alunos = alunoService.listarTodos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscarPorId(@PathVariable Long id) {
        AlunoDTO aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(aluno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> atualizar(@PathVariable Long id, @RequestBody AlunoDTO alunoDTO) {
        alunoDTO.setId(id);
        AlunoDTO alunoAtualizado = alunoService.atualizar(id, alunoDTO);
        return ResponseEntity.ok(alunoAtualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
