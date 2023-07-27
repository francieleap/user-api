package com.example.userapi.api.validator;

import com.example.userapi.api.annotation.CPF;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPFValidador implements ConstraintValidator<CPF, String> {
    public static final String CPF_REGEXP = "[0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}[-]?[0-9]{2}";

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf != null) {

            if (!cpf.matches(CPF_REGEXP)) return false;

            cpf = cpf.replaceAll("[^0-9]", "");

            if (cpf.length() != 11) return false;

            if (cpf.matches("(\\d)\\1{10}")) return false;

            int digito1 = calcularDigitoVerificador(cpf.substring(0, 9));

            int digito2 = calcularDigitoVerificador(cpf.substring(0, 9) + digito1);

            return cpf.endsWith(Integer.toString(digito1).concat(Integer.toString(digito2)));

        }
        return true;
    }

    private static int calcularDigitoVerificador(String digits) {
        int soma = 0;
        int peso = digits.length() + 1;

        for (int i = 0; i < digits.length(); i++) {
            soma += Character.getNumericValue(digits.charAt(i)) * peso--;
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

}
