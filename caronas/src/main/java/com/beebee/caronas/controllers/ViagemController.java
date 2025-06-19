package com.beebee.caronas.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.beebee.caronas.dto.ViagemDTO;
import com.beebee.caronas.services.ViagemService;

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
        viagemService.deletar(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensagem", "Viagem deletada com sucesso");
        return ResponseEntity.ok(response);
    }
}
