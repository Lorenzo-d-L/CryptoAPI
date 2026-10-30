package com.ldldevelopment.cryptoapi.exception;

public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(String ticker) {
        super("Currency not found: " + ticker);
    }
}
