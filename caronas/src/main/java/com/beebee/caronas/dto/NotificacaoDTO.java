package com.beebee.caronas.dto;

import java.time.LocalDateTime;
import com.beebee.caronas.entities.Notificacao;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificacaoDTO {
    private Long id;
    private String mensagem;
    private LocalDateTime data;
    private boolean lida;
    private Long destinatarioId;
    private String link;

    public static NotificacaoDTO fromEntity(Notificacao entity) {
        return NotificacaoDTO.builder()
                .id(entity.getId())
                .mensagem(entity.getMensagem())
                .data(entity.getData())
                .lida(entity.isLida())
                .destinatarioId(entity.getDestinatario().getId())
                .link(entity.getLink())
                .build();
    }
}