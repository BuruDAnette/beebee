package com.beebee.caronas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbViagemAluno")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViagemAluno {
    public enum Situacao {
        SOLICITADA, PENDENTE, CONFIRMADA, CANCELADA, FINALIZADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataConfirmacao;

    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situacao situacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAluno", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idViagem", nullable = false)
    private Viagem viagem;

}