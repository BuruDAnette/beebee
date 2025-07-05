package com.beebee.caronas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private Double mediaCaroneiro;
    private Double mediaCaronista;
    private String login;
}
