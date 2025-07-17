package com.beebee.caronas.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoCadastroDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Login é obrigatório")
    @Size(min = 4, max = 20, message = "Login deve ter entre 4 e 20 caracteres")
    private String login;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 30, message = "Senha deve ter entre 6 e 30 caracteres")
    private String senha;
}