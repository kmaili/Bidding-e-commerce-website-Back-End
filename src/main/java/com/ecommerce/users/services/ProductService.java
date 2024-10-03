package com.ecommerce.users.services;

import com.ecommerce.users.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto addProduct(ProductDto product);
    ProductDto updateProduct(ProductDto product);
    boolean deleteProduct(String id);
    Page<ProductDto> getUserProductsByPage(Long owner, Pageable pageable);
    Page<ProductDto> getUserProductsByPage(Long owner, String searchQuery, Pageable pageable);
}
