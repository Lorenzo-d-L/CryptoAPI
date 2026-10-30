package com.ldldevelopment.cryptoapi.exception;

public class CurrencyAlreadyExistsException extends RuntimeException {

    public CurrencyAlreadyExistsException(String ticker) {
        super("Currency already exists: " + ticker);
    }
}
