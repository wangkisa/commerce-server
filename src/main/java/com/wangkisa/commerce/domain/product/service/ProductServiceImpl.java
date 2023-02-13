package com.wangkisa.commerce.domain.product.service;

import com.wangkisa.commerce.domain.product.code.ProductErrorCode;
import com.wangkisa.commerce.domain.product.dto.PageRequestDTO;
import com.wangkisa.commerce.domain.product.dto.ProductDTO;
import com.wangkisa.commerce.domain.product.entity.Product;
import com.wangkisa.commerce.domain.product.repository.OptimisticLockProductRepository;
import com.wangkisa.commerce.domain.product.repository.PessimisticLockProductRepository;
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
    private final PessimisticLockProductRepository pessimisticLockProductRepository;
    private final OptimisticLockProductRepository optimisticLockProductRepository;

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

    /**
     * 동시성 이슈 스터디용 - 일반 재고 감소
     */
    @Override
    @Transactional
    public void subtractQuantity(final Long id, final Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
        product.subtractQuantity(quantity);
    }

    /**
     * 동시성 이슈 스터디용 - synchronized 이용한 재고 감소
     */
    @Override
    public synchronized void synchronizedSubtractQuantity(final Long id, final Integer quantity) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
        product.subtractQuantity(quantity);
        productRepository.saveAndFlush(product);
    }

    /**
     * 동시성 이슈 스터디용 - Pessimistic Lock 이용한 재고 감소
     */
    @Override
    @Transactional
    public void pessimisticLockSubtractQuantity(final Long id, final Integer quantity) {
        Product product = pessimisticLockProductRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
        product.subtractQuantity(quantity);
    }

    /**
     * 동시성 이슈 스터디용 - Optimistic Lock 이용한 재고 감소
     */
    @Override
    @Transactional
    public void optimisticLockSubtractQuantity(final Long id, final Integer quantity) {
        Product product = optimisticLockProductRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
        product.subtractQuantity(quantity);
    }

    /**
     * 동시성 이슈 스터디용 - Redisson Lock 이용한 재고 감소
     */
    @Override
    @Transactional
    public synchronized void synchronizedSubtractQuantity2(final Long id, final Integer quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductErrorCode.ERROR_NOT_FOUND_PRODUCT));
        product.subtractQuantity(quantity);
    }
}
