package com.beebee.caronas.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbHorarioAcademico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioAcademico {
    public enum DiaSemana {
        SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;

    @Column(nullable = false)
    private String horario;

    private String situacao;

    @ManyToOne
    @JoinColumn(name = "idAluno", nullable = false)
    private Aluno aluno;
}