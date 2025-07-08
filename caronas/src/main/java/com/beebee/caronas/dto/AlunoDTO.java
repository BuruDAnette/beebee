package com.beebee.caronas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 30, message = "Senha deve ter entre 6 e 30 caracteres")
    private String senha;

}
