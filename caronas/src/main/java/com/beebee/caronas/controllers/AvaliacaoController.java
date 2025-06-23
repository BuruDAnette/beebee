package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.AvaliacaoDTO;
import com.beebee.caronas.services.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {
    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listarTodos() {
        return ResponseEntity.ok(avaliacaoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criar(@RequestBody AvaliacaoDTO dto) {
        AvaliacaoDTO criado = avaliacaoService.salvar(dto);
        return ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> atualizar(@PathVariable Long id, @RequestBody AvaliacaoDTO dto) {
        dto.setId(id);
        AvaliacaoDTO atualizado = avaliacaoService.salvar(dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
