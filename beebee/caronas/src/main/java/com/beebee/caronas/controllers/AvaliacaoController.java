package com.beebee.caronas.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beebee.caronas.dto.AvaliacaoDTO;
import com.beebee.caronas.services.AvaliacaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> save(@RequestBody AvaliacaoDTO dto) {
        return ResponseEntity.ok(avaliacaoService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> getAll() {
        return ResponseEntity.ok(avaliacaoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> update(@PathVariable Long id, @RequestBody AvaliacaoDTO dto) {
        return ResponseEntity.ok(avaliacaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        avaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
