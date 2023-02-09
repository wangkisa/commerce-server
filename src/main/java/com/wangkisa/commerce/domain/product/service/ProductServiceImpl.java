package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.dto.PageRequestDTO;
import com.wangkisa.commerce.domain.product.dto.ProductDTO;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.PessimisticProductRepository;
import com.wangkisa.commerce.domain.product.repository.ProductRepository;
import com.wangkisa.commerce.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PessimisticProductRepository pessimisticProductRepository;

    /**
     * 상품 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDTO.ResProductList getProductList(PageRequestDTO pageRequestDTO) {
        Page<Product> productPage = productRepository.findAllByPageRequestDTO(PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize()));

        return ProductDTO.ResProductList.fromPageProductList(productPage);
    }

    /**
     * 상품 상세 조회
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDTO.ResProductDetail getProductDetail(ProductDTO.ReqProductDetail reqProductDetail) {

        Product product = productRepository.findById(reqProductDetail.getProductId())
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));

        return ProductDTO.ResProductDetail.fromProduct(product);
    }

    @Override
    public synchronized void synchronizedSubtractQuantity(final Long id, final Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
        product.subtractQuantity(quantity);
        productRepository.saveAndFlush(product);
    }

    @Override
    @Transactional
    public void pessimisticLockSubtractQuantity(Long id, Integer quantity) {
        Product product = pessimisticProductRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
        product.subtractQuantity(quantity);
    }
}
