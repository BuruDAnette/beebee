package com.beebee.caronas.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbAluno")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluno {
    public enum StatusCadastro { 
        PENDENTE, 
        ATIVO,    
        DESATIVADO 
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    private Double mediaMotorista;
    private Double mediaCaronista;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCadastro statusCadastro;
}
