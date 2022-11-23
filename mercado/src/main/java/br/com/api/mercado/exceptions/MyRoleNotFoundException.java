package br.com.api.mercado.exceptions;

public class MyRoleNotFoundException extends RuntimeException {
    public MyRoleNotFoundException(String message) {
        super(message);
    }
}
