package com.beebee.caronas.dto;

import java.time.LocalDateTime;

import com.beebee.caronas.entities.ViagemAluno;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViagemAlunoDTO {
    private Long id;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataConfirmacao;
    private String observacao;
    private ViagemAluno.Situacao situacao;
    private Long alunoId;
    private Long viagemId;
}
