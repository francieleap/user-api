package com.example.userapi.api.dto;

import com.example.userapi.api.annotation.CPF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "O nome é obrigatório!")
    @Size(max = 100, message = "O email não pode ultrapassar mais que 100 catacteres!" )
    private String name;

    @NotBlank(message = "O email é obrigatorio!")
    @Email(message = "O email informado é inválido!")
    @Size(max = 50, message = "O email não pode ultrapassar mais que 50 catacteres!" )
    private String email;

    @NotBlank(message = "O cpf é obrigatório!")
    @CPF
    private String cpf;

    @NotNull(message = "A idade é obrigatória!")
    @Positive(message = "A idade deve ser um número inteiro positivo")
    @Min(value = 18, message = "A idade do usuário não pode ser menor que 18!")
    @Max(value = 150, message = "A idade do usuário não pode ser maior que 150!")
    private Integer age;
}
