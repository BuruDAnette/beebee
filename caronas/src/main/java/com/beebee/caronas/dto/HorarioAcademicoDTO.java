package com.beebee.caronas.dto;

import com.beebee.caronas.entities.HorarioAcademico;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioAcademicoDTO {
    private Long id;
    private String descricao;
    private HorarioAcademico.DiaSemana dia;
    private String horario;
    private String situacao;
    private Long idAluno;
}