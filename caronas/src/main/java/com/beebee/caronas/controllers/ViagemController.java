package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.services.ViagemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {
    private final ViagemService viagemService;

    // ... os métodos @GetMapping, @PostMapping e @DeleteMapping permanecem os mesmos ...

    @GetMapping
    public ResponseEntity<List<ViagemDTO>> getAll(
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(viagemService.getAll(origem, destino, data));
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

    // MÉTODO PUT AJUSTADO
    @PutMapping("/{id}")
    public ResponseEntity<ViagemDTO> update(@PathVariable Long id, @Valid @RequestBody ViagemDTO dto) {
        ViagemDTO updatedTrip = viagemService.update(id, dto);
        return ResponseEntity.ok(updatedTrip);
    }
    
    // NOVO ENDPOINT
    @PostMapping("/{id}/iniciar")
    public ResponseEntity<ViagemDTO> iniciarViagem(@PathVariable Long id) {
        ViagemDTO updatedTrip = viagemService.iniciarViagem(id);
        return ResponseEntity.ok(updatedTrip);
    }

    // NOVO ENDPOINT
    @PostMapping("/{id}/encerrar")
    public ResponseEntity<ViagemDTO> encerrarViagem(@PathVariable Long id) {
        ViagemDTO updatedTrip = viagemService.encerrarViagem(id);
        return ResponseEntity.ok(updatedTrip);
    }

    // NOVO ENDPOINT
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<ViagemDTO> cancelarViagem(@PathVariable Long id) {
        ViagemDTO updatedTrip = viagemService.cancelarViagem(id);
        return ResponseEntity.ok(updatedTrip);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        viagemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/historico/motorista/{motoristaId}")
    public ResponseEntity<List<ViagemDTO>> getHistoricoByMotoristaId(@PathVariable Long motoristaId) {
        return ResponseEntity.ok(viagemService.getHistoricoByMotoristaId(motoristaId));
    }
}