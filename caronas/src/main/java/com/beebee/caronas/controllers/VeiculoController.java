package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.VeiculoDTO;
import com.beebee.caronas.services.VeiculoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {
    private final VeiculoService veiculoService;

    @GetMapping
    public ResponseEntity<List<VeiculoDTO>> getAll() {
        return ResponseEntity.ok(veiculoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(veiculoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> save(@Valid @RequestBody VeiculoDTO dto) {
        VeiculoDTO savedVehicle = veiculoService.save(dto);
        return ResponseEntity.status(201).body(savedVehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> update(@PathVariable Long id, @Valid @RequestBody VeiculoDTO dto) {
        dto.setId(id);
        VeiculoDTO updatedVehicle = veiculoService.update(id, dto);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
