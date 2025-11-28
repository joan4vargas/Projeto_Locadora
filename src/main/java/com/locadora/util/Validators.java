package com.locadora.util;

public class Validators {

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPlaca(String placa) {
        return placa != null && placa.matches("^[A-Z0-9-]{4,8}$");
    }
}
