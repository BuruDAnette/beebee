package com.beebee.caronas.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbViagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Viagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private LocalDateTime  dataInicio;

    private LocalDateTime  dataFim;

    private String origem;

    private String destino;

    private String situacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAluno", nullable = false)
    private Aluno motorista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idVeiculo")
    private Veiculo veiculo;
}
