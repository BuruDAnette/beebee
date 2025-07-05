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

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.services.ViagemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemService viagemService;

    @PostMapping
    public ResponseEntity<ViagemDTO> save(@RequestBody ViagemDTO dto) {
        return ResponseEntity.status(201).body(viagemService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<ViagemDTO>> getAll() {
        return ResponseEntity.ok(viagemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(viagemService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemDTO> update(@PathVariable Long id, @RequestBody ViagemDTO dto) {
        return ResponseEntity.ok(viagemService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viagemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
