package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.services.ViagemService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {
    private final ViagemService viagemService;

    @GetMapping
    public ResponseEntity<List<ViagemDTO>> listarTodos() {
        return ResponseEntity.ok(viagemService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemDTO> buscarPorId(@PathVariable Long id) {
        ViagemDTO dto = viagemService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ViagemDTO> criar(@RequestBody ViagemDTO dto) {
        ViagemDTO criado = viagemService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemDTO> atualizar(@PathVariable Long id, @RequestBody ViagemDTO dto) {
        dto.setId(id);
        ViagemDTO atualizado = viagemService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        viagemService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
