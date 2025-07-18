package com.beebee.caronas.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import com.beebee.caronas.entities.Aluno;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoDTO {
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @Min(value = 1, message = "Média do motorista não pode ser negativa")
    @Max(value = 5, message = "Média do motorista não pode ser maior que 5")
    private Double mediaMotorista;

    @Min(value = 1, message = "Média do caronista não pode ser negativa")
    @Max(value = 5, message = "Média do caronista não pode ser maior que 5")
    private Double mediaCaronista;

    @NotBlank(message = "Login é obrigatório")
    @Size(min = 4, max = 20, message = "Login deve ter entre 4 e 20 caracteres")
    private String login;

    private String senha;
}
