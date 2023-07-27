package com.example.userapi.model.entity;

import com.example.userapi.api.annotation.CPF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_USERS")
public class User implements Serializable {

    private static final long serialVersionUID = 2450385951771325613L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "O nome é obrigatório!")
    @Size(max = 100, message = "O email não pode ultrapassar mais que 100 catacteres!" )
    private String name;

    @NotBlank(message = "O email é obrigatorio!")
    @Email(message = "O email informado é inválido!")
    @Size(max = 50, message = "O email não pode ultrapassar mais que 50 catacteres!" )
    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "cpf", unique = true, nullable = false, length = 14)
    @NotBlank(message = "O cpf é obrigatório!")
    @CPF
    private String cpf;

    @Column(name = "age", nullable = false )
    @NotNull(message = "A idade é obrigatória!")
    @Positive(message = "A idade deve ser um número inteiro positivo")
    @Min(value = 18, message = "A idade do usuário não pode ser menor que 18!")
    @Max(value = 150, message = "A idade do usuário não pode ser maior que 150!")
    private Integer age;
}
