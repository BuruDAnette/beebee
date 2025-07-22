package com.beebee.caronas.services;

import com.beebee.caronas.dto.NotificacaoDTO;
import com.beebee.caronas.entities.Aluno;
import com.beebee.caronas.entities.Notificacao;
import com.beebee.caronas.repositories.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    @Transactional
    public void criarNotificacao(Aluno destinatario, String mensagem, String link) {
        Notificacao notificacao = Notificacao.builder()
                .destinatario(destinatario)
                .mensagem(mensagem)
                .data(LocalDateTime.now())
                .lida(false)
                .link(link)
                .build();
        notificacaoRepository.save(notificacao);
    }

    @Transactional(readOnly = true)
    public List<NotificacaoDTO> findByDestinatarioId(Long destinatarioId) {
        return notificacaoRepository.findByDestinatarioIdOrderByDataDesc(destinatarioId)
                .stream()
                .map(NotificacaoDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void marcarComoLida(Long notificacaoId) {
        notificacaoRepository.findById(notificacaoId).ifPresent(notificacao -> {
            notificacao.setLida(true);
            notificacaoRepository.save(notificacao);
        });
    }
}