package com.ldldevelopment.cryptoapi.repository;

import com.ldldevelopment.cryptoapi.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Paging and sorting come from JpaRepository, no custom queries needed yet.
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
}
