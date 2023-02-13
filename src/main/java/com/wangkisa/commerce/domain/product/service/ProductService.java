package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.dto.PageRequestDTO;
import com.wangkisa.commerce.domain.product.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

public interface ProductService {
    ProductDTO.ResProductList getProductList(PageRequestDTO pageRequestDTO);

    ProductDTO.ResProductDetail getProductDetail(ProductDTO.ReqProductDetail reqProductDetail);

    void synchronizedSubtractQuantity(Long productId, Integer quantity);

    void pessimisticLockSubtractQuantity(Long productId, Integer quantity);

    void optimisticLockSubtractQuantity(Long productId, Integer quantity);

    void synchronizedSubtractQuantity2(Long id, Integer quantity);
}
