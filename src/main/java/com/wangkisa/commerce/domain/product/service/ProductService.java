package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.dto.ProductDTO;

public interface ProductService {
    ProductDTO.ResProductList getProductList();

    ProductDTO.ResProductDetail getProductDetail(ProductDTO.ReqProductDetail reqProductDetail);
}
