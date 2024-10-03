package com.ecommerce.users.services;

import com.ecommerce.users.dto.ProductDto;
import com.ecommerce.users.entities.ProductEntity;
import com.ecommerce.users.mappers.ProductMapper;
import com.ecommerce.users.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductDto addProduct(ProductDto product) {
        ProductEntity productEntity = productRepository.save(productMapper.toEntity(product));
        return productMapper.toDto(productEntity);
    }

    @Override
    public ProductDto updateProduct(ProductDto product) {
        ProductEntity productEntity = productRepository.save(productMapper.toEntity(product));
        return productMapper.toDto(productEntity);
    }

    @Override
    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        }
        else
            return false;
    }

    @Override
    public Page<ProductDto> getUserProductsByPage(Long owner, Pageable pageable) {
        Page<ProductEntity> page = productRepository.findAllByOwner(owner, pageable);
        return page.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> getUserProductsByPage(Long owner, String searchQuery, Pageable pageable) {
        Page<ProductEntity> page = productRepository.findByOwnerAndSearchQuery(searchQuery, owner, pageable);
        return page.map(productMapper::toDto);
    }
}
