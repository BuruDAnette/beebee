package com.beebee.caronas.dto;

import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoDTO {
    private Long id;
    private String descricao;
    private LocalDate data;
    private String comentarioCaroneiro;
    private Double notaCaroneiro;
    private String comentarioCaronista;
    private Double notaCaronista;
    private Long viagemAlunoId;
}
