package com.beebee.caronas.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbVeiculo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    private String modelo;

    private String cor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAluno", nullable = false)
    private Aluno motorista;
}
