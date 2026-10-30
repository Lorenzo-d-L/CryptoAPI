package com.ldldevelopment.cryptoapi.controller;

import com.ldldevelopment.cryptoapi.dto.CurrencyRequest;
import com.ldldevelopment.cryptoapi.dto.CurrencyUpdateRequest;
import com.ldldevelopment.cryptoapi.dto.PageResponse;
import com.ldldevelopment.cryptoapi.entity.Currency;
import com.ldldevelopment.cryptoapi.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity<Currency> create(@Valid @RequestBody CurrencyRequest request) {
        Currency created = currencyService.create(request.toEntity());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{ticker}")
                .buildAndExpand(created.getTicker())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{ticker}")
    public Currency get(@PathVariable String ticker) {
        return currencyService.getByTicker(ticker);
    }

    @GetMapping
    public PageResponse<Currency> list(@PageableDefault(sort = "ticker") Pageable pageable) {
        return PageResponse.from(currencyService.getAll(pageable));
    }

    @PutMapping("/{ticker}")
    public Currency update(@PathVariable String ticker, @Valid @RequestBody CurrencyUpdateRequest request) {
        return currencyService.update(ticker, request.toEntity());
    }

    @DeleteMapping("/{ticker}")
    public ResponseEntity<Void> delete(@PathVariable String ticker) {
        currencyService.delete(ticker);
        return ResponseEntity.noContent().build();
    }
}
