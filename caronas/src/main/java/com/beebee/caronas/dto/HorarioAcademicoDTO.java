package com.beebee.caronas.dto;

import com.beebee.caronas.entities.HorarioAcademico;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioAcademicoDTO {
    private Long id;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 100, message = "Descrição não pode ultrapassar 100 caracteres")
    private String descricao;

    @NotNull(message = "Dia da semana é obrigatório")
    private HorarioAcademico.DiaSemana dia;

    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    private String horario;
    private String situacao;

    @NotNull(message = "ID do aluno é obrigatório")
    private Long idAluno;
}