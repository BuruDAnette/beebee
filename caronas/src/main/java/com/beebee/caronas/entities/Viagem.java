package com.beebee.caronas.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private String origem;

    private String destino;

    private String situacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAluno", nullable = false)
    private Aluno motorista;
}
