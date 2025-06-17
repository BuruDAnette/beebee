package com.beebee.caronas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VeiculoDTO {

    private Long id;
    private String placa;
    private String modelo;
    private String cor;
    private Long motoristaId;
}
