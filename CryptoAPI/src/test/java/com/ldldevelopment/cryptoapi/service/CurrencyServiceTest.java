package com.ldldevelopment.cryptoapi.service;

import com.ldldevelopment.cryptoapi.entity.Currency;
import com.ldldevelopment.cryptoapi.exception.CurrencyAlreadyExistsException;
import com.ldldevelopment.cryptoapi.exception.CurrencyNotFoundException;
import com.ldldevelopment.cryptoapi.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(currencyRepository);
    }

    private Currency bitcoin() {
        return Currency.builder()
                .ticker("BTC")
                .name("Bitcoin")
                .numberOfCoins(new BigDecimal("16770000"))
                .marketCap(new BigDecimal("189580000000"))
                .build();
    }

    @Test
    void createSavesNewCurrency() {
        Currency bitcoin = bitcoin();
        when(currencyRepository.existsById("BTC")).thenReturn(false);
        when(currencyRepository.save(bitcoin)).thenReturn(bitcoin);

        Currency result = currencyService.create(bitcoin);

        assertThat(result).isEqualTo(bitcoin);
    }

    @Test
    void createRejectsDuplicateTicker() {
        when(currencyRepository.existsById("BTC")).thenReturn(true);

        assertThatThrownBy(() -> currencyService.create(bitcoin()))
                .isInstanceOf(CurrencyAlreadyExistsException.class);
        verify(currencyRepository, never()).save(any());
    }

    @Test
    void getByTickerReturnsCurrency() {
        when(currencyRepository.findById("BTC")).thenReturn(Optional.of(bitcoin()));

        assertThat(currencyService.getByTicker("BTC").getName()).isEqualTo("Bitcoin");
    }

    @Test
    void getByTickerThrowsWhenMissing() {
        when(currencyRepository.findById("ZZZ")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> currencyService.getByTicker("ZZZ"))
                .isInstanceOf(CurrencyNotFoundException.class);
    }

    @Test
    void updateChangesFieldsButKeepsTicker() {
        when(currencyRepository.findById("BTC")).thenReturn(Optional.of(bitcoin()));
        when(currencyRepository.save(any(Currency.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Currency changes = Currency.builder()
                .name("Bitcoin")
                .numberOfCoins(new BigDecimal("16770001"))
                .marketCap(new BigDecimal("190000000000"))
                .build();

        Currency updated = currencyService.update("BTC", changes);

        assertThat(updated.getTicker()).isEqualTo("BTC");
        assertThat(updated.getNumberOfCoins()).isEqualByComparingTo("16770001");
        assertThat(updated.getMarketCap()).isEqualByComparingTo("190000000000");
    }

    @Test
    void updateThrowsWhenMissing() {
        when(currencyRepository.findById("ZZZ")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> currencyService.update("ZZZ", bitcoin()))
                .isInstanceOf(CurrencyNotFoundException.class);
    }

    @Test
    void deleteRemovesExistingCurrency() {
        when(currencyRepository.existsById("BTC")).thenReturn(true);

        currencyService.delete("BTC");

        verify(currencyRepository).deleteById("BTC");
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(currencyRepository.existsById("ZZZ")).thenReturn(false);

        assertThatThrownBy(() -> currencyService.delete("ZZZ"))
                .isInstanceOf(CurrencyNotFoundException.class);
        verify(currencyRepository, never()).deleteById(any());
    }
}
