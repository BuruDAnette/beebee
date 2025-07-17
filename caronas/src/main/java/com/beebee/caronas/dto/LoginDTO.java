package com.beebee.caronas.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {
    private String login;
    private String senha;
}
