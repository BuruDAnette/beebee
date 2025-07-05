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
    public ResponseEntity<ViagemAlunoDTO> buscarPorId(@PathVariable Long id) {
        ViagemAlunoDTO viagemAluno = viagemAlunoService.buscarPorId(id);
        return ResponseEntity.ok(viagemAluno);
    }

    @PostMapping
    public ResponseEntity<ViagemAlunoDTO> salvar(@RequestBody ViagemAlunoDTO dto) {
        ViagemAlunoDTO viagemSalva = viagemAlunoService.salvar(dto);
        return ResponseEntity.ok(viagemSalva);
    }

    @GetMapping
    public ResponseEntity<List<ViagemAlunoDTO>> listarTodos() {
        List<ViagemAlunoDTO> viagensAlunos = viagemAlunoService.listarTodos();
        return ResponseEntity.ok(viagensAlunos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemAlunoDTO> atualizar(
            @PathVariable Long id, 
            @RequestBody ViagemAlunoDTO dto) {
        dto.setId(id);
        ViagemAlunoDTO viagemAlunoAtualizada = viagemAlunoService.atualizar(id, dto);
        return ResponseEntity.ok(viagemAlunoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        viagemAlunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}