package com.beebee.caronas.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeiculoDTO {
    private Long id;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "[A-Z]{3}[0-9][A-Z0-9][0-9]{2}", message = "Placa deve estar no formato AAA1A11 ou AAA1111")
    private String placa;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 50, message = "Modelo não pode ultrapassar 50 caracteres")
    private String modelo;

    @Size(max = 30, message = "Cor não pode ultrapassar 30 caracteres")
    private String cor;

    @NotNull(message = "ID do motorista é obrigatório")
    private Long motoristaId;
}
