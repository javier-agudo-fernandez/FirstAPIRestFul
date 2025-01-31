package com.accesodatos.firstapirestful.util;

import java.util.List;

public class Validador {
    public static boolean validarDocumentoIdentidad(String di) {
        // Validar que el documento tiene 9 caracteres
        if (di == null || di.length() != 9) {
            return false;
        }

        // Lista de Letras
        List<Character> letras = List.of(
                'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B',
                'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'
        );

        int diDigits;
        char letraInicio = di.charAt(0); // Verificar si es extranjero o español mediante el primer caracter

        try {
            // NIE
            if (Character.isLetter(letraInicio)) {
                diDigits = Integer.parseInt(getNumeroEquivalenteNIE(letraInicio) + di.substring(1, 8));
            }
            // DNI
            else {
                diDigits = Integer.parseInt(di.substring(0, 8));
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // Calcular el resto de la división del número por 23
        int resto = diDigits % 23;

        // Obtener la letra válida para ese número
        char letraEsperada = letras.get(resto);

        // Obtener la letra introducida
        char letraIntroducida = di.charAt(8);

        // Validar que coinciden
        if (letraIntroducida != letraEsperada) {
            return false;
        }
        return true;
    }

    // Método auxiliar para obtener el equivalente de las letras de NIF
    private static int getNumeroEquivalenteNIE(char letraInicio) {
        return switch (letraInicio) {
            case 'X' -> 0;
            case 'Y' -> 1;
            case 'Z' -> 2;
            default -> throw new IllegalArgumentException("Caracter no válido: " + letraInicio);
        };
    }
}
