package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.services.ViagemService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {
    @Autowired
    private ViagemService viagemService;

    @GetMapping
    public ResponseEntity<List<ViagemDTO>> listarTodos() {
        return ResponseEntity.ok(viagemService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ViagemDTO dto = viagemService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ViagemDTO dto) {
        ViagemDTO criado = viagemService.salvar(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Viagem criada com sucesso");
        response.put("viagem", criado);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ViagemDTO dto) {
        dto.setId(id);
        ViagemDTO atualizado = viagemService.salvar(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Viagem atualizada com sucesso");
        response.put("viagem", atualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        viagemService.excluir(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Viagem deletada com sucesso");
        return ResponseEntity.ok(response);
    }
}
