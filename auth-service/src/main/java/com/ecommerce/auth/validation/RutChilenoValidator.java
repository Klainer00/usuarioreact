package com.ecommerce.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RutChilenoValidator implements ConstraintValidator<RutChileno, String> {

    @Override
    public boolean isValid(String rut, ConstraintValidatorContext context) {
        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }

        // Limpiar el RUT (eliminar puntos y guiones)
        String rutLimpio = rut.replaceAll("[.-]", "").toUpperCase();

        if (rutLimpio.length() < 2) {
            return false;
        }

        String cuerpo = rutLimpio.substring(0, rutLimpio.length() - 1);
        char dv = rutLimpio.charAt(rutLimpio.length() - 1);

        try {
            int rutNumerico = Integer.parseInt(cuerpo);
            return validarDigitoVerificador(rutNumerico, dv);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validarDigitoVerificador(int rut, char dv) {
        int suma = 0;
        int multiplo = 2;

        while (rut > 0) {
            suma += (rut % 10) * multiplo;
            rut /= 10;
            multiplo++;
            if (multiplo == 8) {
                multiplo = 2;
            }
        }

        int resto = suma % 11;
        int digitoVerificadorCalculado = 11 - resto;

        char dvCalculado;
        if (digitoVerificadorCalculado == 11) {
            dvCalculado = '0';
        } else if (digitoVerificadorCalculado == 10) {
            dvCalculado = 'K';
        } else {
            dvCalculado = Character.forDigit(digitoVerificadorCalculado, 10);
        }

        return dv == dvCalculado;
    }
}
