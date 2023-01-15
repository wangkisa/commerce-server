package com.wangkisa.commerce.domain.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class PageRequestDTO {

    private Integer page;
    private Integer size;

    public static PageRequestDTO of(Integer page, Integer size) {
        return new PageRequestDTO(page, size);
    }

    public PageRequestDTO() {
        if (page == null) page = 1;
        if (size == null) size = 10;
    }

    public PageRequestDTO(Integer page, Integer size) {
        if (page == null) page = 1;
        if (size == null) size = 10;
        this.page = page;
        this.size = size;
    }

    public Pageable toPageable() {
        return PageRequest.of(this.page - 1, size);
    }
}