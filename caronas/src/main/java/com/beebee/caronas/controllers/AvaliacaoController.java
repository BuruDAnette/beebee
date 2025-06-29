package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.AvaliacaoDTO;
import com.beebee.caronas.services.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {
    private final AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> getAll() {
        return ResponseEntity.ok(avaliacaoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<AvaliacaoDTO> save(@RequestBody AvaliacaoDTO dto) {
        AvaliacaoDTO savedRating = avaliacaoService.save(dto);
        return ResponseEntity.status(201).body(savedRating);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> update(@PathVariable Long id, @RequestBody AvaliacaoDTO dto) {
        dto.setId(id);
        AvaliacaoDTO updatedRating = avaliacaoService.update(id, dto);
        return ResponseEntity.ok(updatedRating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        avaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
