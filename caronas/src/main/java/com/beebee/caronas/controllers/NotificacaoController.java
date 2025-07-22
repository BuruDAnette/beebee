package com.beebee.caronas.controllers;

import com.beebee.caronas.dto.NotificacaoDTO;
import com.beebee.caronas.services.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping("/aluno/{destinatarioId}")
    public ResponseEntity<List<NotificacaoDTO>> getNotificacoes(@PathVariable Long destinatarioId) {
        List<NotificacaoDTO> notificacoes = notificacaoService.findByDestinatarioId(destinatarioId);
        return ResponseEntity.ok(notificacoes);
    }

    @PostMapping("/{notificacaoId}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Long notificacaoId) {
        notificacaoService.marcarComoLida(notificacaoId);
        return ResponseEntity.noContent().build();
    }
}