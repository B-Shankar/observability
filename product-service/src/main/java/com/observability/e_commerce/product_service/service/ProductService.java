package com.observability.e_commerce.product_service.service;

import com.observability.e_commerce.product_service.entity.Product;
import com.observability.e_commerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product save(Product product) {
        log.info("Saving product: {}", product.getName());
        return productRepository.save(product);
    }

    public Product getById(Long id) {
        log.info("Fetching product with id: {}", id);

        // slow downstream dependency
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getAll() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

}
