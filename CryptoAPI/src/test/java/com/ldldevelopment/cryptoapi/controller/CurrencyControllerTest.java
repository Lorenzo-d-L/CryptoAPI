package com.ldldevelopment.cryptoapi.controller;

import com.ldldevelopment.cryptoapi.entity.Currency;
import com.ldldevelopment.cryptoapi.exception.CurrencyAlreadyExistsException;
import com.ldldevelopment.cryptoapi.exception.CurrencyNotFoundException;
import com.ldldevelopment.cryptoapi.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    private Currency bitcoin() {
        return Currency.builder()
                .ticker("BTC")
                .name("Bitcoin")
                .numberOfCoins(new BigDecimal("16770000"))
                .marketCap(new BigDecimal("189580000000"))
                .build();
    }

    @Test
    void createReturnsCreatedWithLocation() throws Exception {
        when(currencyService.create(any(Currency.class))).thenReturn(bitcoin());

        mockMvc.perform(post("/api/currencies")
                        .contentType("application/json")
                        .content("""
                                {"ticker":"BTC","name":"Bitcoin","numberOfCoins":16770000,"marketCap":189580000000}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/currencies/BTC")))
                .andExpect(jsonPath("$.ticker").value("BTC"));
    }

    @Test
    void createWithBlankTickerReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/currencies")
                        .contentType("application/json")
                        .content("""
                                {"ticker":"","name":"Bitcoin","numberOfCoins":1,"marketCap":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithExistingTickerReturnsConflict() throws Exception {
        when(currencyService.create(any(Currency.class)))
                .thenThrow(new CurrencyAlreadyExistsException("BTC"));

        mockMvc.perform(post("/api/currencies")
                        .contentType("application/json")
                        .content("""
                                {"ticker":"BTC","name":"Bitcoin","numberOfCoins":1,"marketCap":1}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void getReturnsCurrency() throws Exception {
        when(currencyService.getByTicker("BTC")).thenReturn(bitcoin());

        mockMvc.perform(get("/api/currencies/BTC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    void getUnknownTickerReturnsNotFound() throws Exception {
        when(currencyService.getByTicker("ZZZ")).thenThrow(new CurrencyNotFoundException("ZZZ"));

        mockMvc.perform(get("/api/currencies/ZZZ"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listReturnsPagedContent() throws Exception {
        when(currencyService.getAll(any())).thenReturn(new PageImpl<>(List.of(bitcoin()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/currencies?page=0&size=10&sort=ticker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].ticker").value("BTC"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void updateReturnsUpdatedCurrency() throws Exception {
        when(currencyService.update(eq("BTC"), any(Currency.class))).thenReturn(bitcoin());

        mockMvc.perform(put("/api/currencies/BTC")
                        .contentType("application/json")
                        .content("""
                                {"name":"Bitcoin","numberOfCoins":16770000,"marketCap":189580000000}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("BTC"));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/currencies/BTC"))
                .andExpect(status().isNoContent());
    }
}
