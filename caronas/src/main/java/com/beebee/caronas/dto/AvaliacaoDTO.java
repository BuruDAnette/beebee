package com.beebee.caronas.dto;

import java.time.LocalDate;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoDTO {
    private Long id;

    @Size(max = 500, message = "Descrição não pode ultrapassar 500 caracteres")
    private String descricao;

    @PastOrPresent(message = "Data não pode ser no futuro")
    private LocalDate data;

    @Size(max = 500, message = "Comentário do motorista não pode ultrapassar 500 caracteres")
    private String comentarioMotorista;

    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    private Integer notaMotorista;

    @Size(max = 500, message = "Comentário do caronista não pode ultrapassar 500 caracteres")
    private String comentarioCaronista;

    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    private Integer notaCaronista;

    @NotNull(message = "ID da viagem do aluno é obrigatório")
    private Long viagemAlunoId;
}
