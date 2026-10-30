package com.ldldevelopment.cryptoapi.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTest {

    @Test
    void builderSetsAllFields() {
        Currency currency = Currency.builder()
                .ticker("BTC")
                .name("Bitcoin")
                .numberOfCoins(new BigDecimal("16770000"))
                .marketCap(new BigDecimal("189580000000"))
                .build();

        assertThat(currency.getTicker()).isEqualTo("BTC");
        assertThat(currency.getName()).isEqualTo("Bitcoin");
        assertThat(currency.getNumberOfCoins()).isEqualByComparingTo("16770000");
        assertThat(currency.getMarketCap()).isEqualByComparingTo("189580000000");
    }

    @Test
    void equalsAndHashCodeAreBasedOnFields() {
        Currency a = Currency.builder().ticker("BTC").name("Bitcoin")
                .numberOfCoins(BigDecimal.ONE).marketCap(BigDecimal.TEN).build();
        Currency b = Currency.builder().ticker("BTC").name("Bitcoin")
                .numberOfCoins(BigDecimal.ONE).marketCap(BigDecimal.TEN).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
