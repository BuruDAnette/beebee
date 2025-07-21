package com.beebee.caronas.dto;

import java.time.LocalDateTime;

import com.beebee.caronas.entities.ViagemAluno;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViagemAlunoDTO {
    private Long id;

    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataConfirmacao;

    @Size(max = 500, message = "Observação não pode ultrapassar 500 caracteres")
    private String observacao;

    @NotNull(message = "Situação é obrigatória")
    private ViagemAluno.Situacao situacao;

    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;

    private String alunoNome;

    @NotNull(message = "A viagem é obrigatória")
    private ViagemDTO viagem;

    public ViagemAlunoDTO(ViagemAluno entity) {
        this.id = entity.getId();
        this.dataSolicitacao = entity.getDataSolicitacao();
        this.dataConfirmacao = entity.getDataConfirmacao();
        this.observacao = entity.getObservacao();
        this.situacao = entity.getSituacao();
        this.alunoId = entity.getAluno().getId();
        this.alunoNome = entity.getAluno().getNome();
        this.viagem = new ViagemDTO(entity.getViagem());
    }
}
