package com.beebee.caronas.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ViagemDTO {
    private Long id;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String origem;
    private String destino;
    private String situacao;
    private Long motoristaId;
}
