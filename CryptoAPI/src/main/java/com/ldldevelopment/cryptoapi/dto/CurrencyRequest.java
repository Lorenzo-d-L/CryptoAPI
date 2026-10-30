package com.ldldevelopment.cryptoapi.dto;

import com.ldldevelopment.cryptoapi.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * POST body for creating a currency. Separate from the entity so ticker is required
 * here but not on update.
 */
public record CurrencyRequest(
        @NotBlank(message = "ticker must not be blank") String ticker,
        @NotBlank(message = "name must not be blank") String name,
        @NotNull @PositiveOrZero(message = "numberOfCoins must be zero or positive") BigDecimal numberOfCoins,
        @NotNull @PositiveOrZero(message = "marketCap must be zero or positive") BigDecimal marketCap) {

    public Currency toEntity() {
        return Currency.builder()
                .ticker(ticker)
                .name(name)
                .numberOfCoins(numberOfCoins)
                .marketCap(marketCap)
                .build();
    }
}
