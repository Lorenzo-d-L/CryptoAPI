package com.ldldevelopment.cryptoapi.dto;

import com.ldldevelopment.cryptoapi.entity.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * PUT body for updating a currency. No ticker field, that comes from the path.
 */
public record CurrencyUpdateRequest(
        @NotBlank(message = "name must not be blank") String name,
        @NotNull @PositiveOrZero(message = "numberOfCoins must be zero or positive") BigDecimal numberOfCoins,
        @NotNull @PositiveOrZero(message = "marketCap must be zero or positive") BigDecimal marketCap) {

    public Currency toEntity() {
        return Currency.builder()
                .name(name)
                .numberOfCoins(numberOfCoins)
                .marketCap(marketCap)
                .build();
    }
}
