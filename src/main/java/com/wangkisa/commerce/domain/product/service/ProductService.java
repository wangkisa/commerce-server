package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.dto.PageRequestDTO;
import com.wangkisa.commerce.domain.product.dto.ProductDTO;

public interface ProductService {
    ProductDTO.ResProductList getProductList(PageRequestDTO pageRequestDTO);

    ProductDTO.ResProductDetail getProductDetail(ProductDTO.ReqProductDetail reqProductDetail);

    void synchronizedSubtractQuantity(Long productId, Integer quantity);

    void pessimisticLockSubtractQuantity(Long productId, Integer quantity);

    void optimisticLockSubtractQuantity(Long productId, Integer quantity);
}
