package com.ldldevelopment.cryptoapi.service;

import com.ldldevelopment.cryptoapi.entity.Currency;
import com.ldldevelopment.cryptoapi.exception.CurrencyAlreadyExistsException;
import com.ldldevelopment.cryptoapi.exception.CurrencyNotFoundException;
import com.ldldevelopment.cryptoapi.repository.CurrencyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Currency create(Currency currency) {
        if (currencyRepository.existsById(currency.getTicker())) {
            throw new CurrencyAlreadyExistsException(currency.getTicker());
        }
        return currencyRepository.save(currency);
    }

    public Currency getByTicker(String ticker) {
        return currencyRepository.findById(ticker)
                .orElseThrow(() -> new CurrencyNotFoundException(ticker));
    }

    public Page<Currency> getAll(Pageable pageable) {
        return currencyRepository.findAll(pageable);
    }

    public Currency update(String ticker, Currency changes) {
        Currency existing = getByTicker(ticker);
        existing.setName(changes.getName());
        existing.setNumberOfCoins(changes.getNumberOfCoins());
        existing.setMarketCap(changes.getMarketCap());
        return currencyRepository.save(existing);
    }

    public void delete(String ticker) {
        if (!currencyRepository.existsById(ticker)) {
            throw new CurrencyNotFoundException(ticker);
        }
        currencyRepository.deleteById(ticker);
    }
}
