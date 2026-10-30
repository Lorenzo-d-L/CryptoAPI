package com.ldldevelopment.cryptoapi.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Plain JSON shape for a page of results, so the API doesn't leak Spring Data's PageImpl.
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
