package com.beebee.caronas.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.beebee.caronas.entities.Viagem;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViagemDTO {
    private Long id;

    @Size(max = 200, message = "Descrição não pode ultrapassar 200 caracteres")
    private String descricao;

    @NotNull(message = "Data de início é obrigatória")
    @FutureOrPresent(message = "Data de início deve ser hoje ou no futuro")
    private LocalDateTime dataInicio;

    @FutureOrPresent(message = "Data de fim deve ser hoje ou no futuro")
    private LocalDateTime dataFim;

    @NotBlank(message = "Origem é obrigatória")
    @Size(max = 100, message = "Origem não pode ultrapassar 100 caracteres")
    private String origem;

    @NotBlank(message = "Destino é obrigatório")
    @Size(max = 100, message = "Destino não pode ultrapassar 100 caracteres")
    private String destino;

    private String situacao;

    @NotNull(message = "ID do motorista é obrigatório")
    private Long motoristaId;
    private String motoristaNome;

    public ViagemDTO(Viagem entity) {
        this.id = entity.getId();
        this.descricao = entity.getDescricao();
        this.dataInicio = entity.getDataInicio();
        this.dataFim = entity.getDataFim();
        this.origem = entity.getOrigem();
        this.destino = entity.getDestino();
        this.situacao = entity.getSituacao().toString();
        this.motoristaId = entity.getMotorista().getId();
        this.motoristaNome = entity.getMotorista().getNome();
    }
}
