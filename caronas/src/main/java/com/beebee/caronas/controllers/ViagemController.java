package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.services.ViagemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {
    private final ViagemService viagemService;

    @GetMapping
    public ResponseEntity<List<ViagemDTO>> getAll() {
        return ResponseEntity.ok(viagemService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> getById(@PathVariable Long id) {
        ViagemDTO dto = viagemService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/motorista/{motoristaId}")
    public ResponseEntity<List<ViagemDTO>> getByMotoristaId(@PathVariable Long motoristaId) {
        return ResponseEntity.ok(viagemService.getByMotoristaId(motoristaId));
    }

    @PostMapping
    public ResponseEntity<ViagemDTO> save(@Valid @RequestBody ViagemDTO dto) {
        ViagemDTO savedTrip = viagemService.save(dto);
        return ResponseEntity.status(201).body(savedTrip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemDTO> update(@PathVariable Long id, @Valid @RequestBody ViagemDTO dto) {
        dto.setId(id);
        ViagemDTO updatedTrip = viagemService.update(id, dto);
        return ResponseEntity.ok(updatedTrip);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viagemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
