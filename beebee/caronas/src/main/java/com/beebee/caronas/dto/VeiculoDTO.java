package com.beebee.caronas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeiculoDTO {
    private Long id;
    private String placa;
    private String modelo;
    private String cor;
    private Long motoristaId;
}
