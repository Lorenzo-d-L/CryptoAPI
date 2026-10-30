package com.ldldevelopment.cryptoapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Crypto currency entity. Ticker is the primary key and the identifier used in the API.
 */
@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @Column(name = "ticker", nullable = false, updatable = false, length = 10)
    @NotBlank(message = "ticker must not be blank")
    private String ticker;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "name must not be blank")
    private String name;

    @Column(name = "number_of_coins", nullable = false, precision = 30, scale = 8)
    @NotNull(message = "numberOfCoins must not be null")
    @PositiveOrZero(message = "numberOfCoins must be zero or positive")
    private BigDecimal numberOfCoins;

    @Column(name = "market_cap", nullable = false, precision = 30, scale = 8)
    @NotNull(message = "marketCap must not be null")
    @PositiveOrZero(message = "marketCap must be zero or positive")
    private BigDecimal marketCap;

    // required by JPA
    protected Currency() {
    }

    private Currency(Builder builder) {
        this.ticker = builder.ticker;
        this.name = builder.name;
        this.numberOfCoins = builder.numberOfCoins;
        this.marketCap = builder.marketCap;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getNumberOfCoins() {
        return numberOfCoins;
    }

    public void setNumberOfCoins(BigDecimal numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency currency)) {
            return false;
        }
        return Objects.equals(ticker, currency.ticker)
                && Objects.equals(name, currency.name)
                && Objects.equals(numberOfCoins, currency.numberOfCoins)
                && Objects.equals(marketCap, currency.marketCap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, name, numberOfCoins, marketCap);
    }

    @Override
    public String toString() {
        return "Currency{"
                + "ticker='" + ticker + '\''
                + ", name='" + name + '\''
                + ", numberOfCoins=" + numberOfCoins
                + ", marketCap=" + marketCap
                + '}';
    }

    public static final class Builder {

        private String ticker;
        private String name;
        private BigDecimal numberOfCoins;
        private BigDecimal marketCap;

        private Builder() {
        }

        public Builder ticker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder numberOfCoins(BigDecimal numberOfCoins) {
            this.numberOfCoins = numberOfCoins;
            return this;
        }

        public Builder marketCap(BigDecimal marketCap) {
            this.marketCap = marketCap;
            return this;
        }

        public Currency build() {
            return new Currency(this);
        }
    }
}
