package com.ldldevelopment.cryptoapi.repository;

import com.ldldevelopment.cryptoapi.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    void dataSqlSeedsFourCurrencies() {
        assertThat(currencyRepository.findAll()).hasSize(4);
        assertThat(currencyRepository.existsById("BTC")).isTrue();
        assertThat(currencyRepository.existsById("ETH")).isTrue();
        assertThat(currencyRepository.existsById("XRP")).isTrue();
        assertThat(currencyRepository.existsById("BCH")).isTrue();
    }

    @Test
    void savesAndFindsByTicker() {
        Currency currency = Currency.builder()
                .ticker("LTC")
                .name("Litecoin")
                .numberOfCoins(new BigDecimal("73000000"))
                .marketCap(new BigDecimal("5000000000"))
                .build();

        currencyRepository.save(currency);

        Optional<Currency> found = currencyRepository.findById("LTC");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Litecoin");
    }

    @Test
    void deletesByTicker() {
        currencyRepository.deleteById("BTC");

        assertThat(currencyRepository.existsById("BTC")).isFalse();
    }
}
