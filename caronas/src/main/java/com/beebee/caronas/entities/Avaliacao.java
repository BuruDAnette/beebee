package com.beebee.caronas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbAvaliacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private LocalDate data;

    private String comentarioMotorista;
    private Integer notaMotorista;

    private String comentarioCaronista;
    private Integer notaCaronista;

    @ManyToOne
    @JoinColumn(name = "idViagemAluno")
    private ViagemAluno viagemAluno;
}
