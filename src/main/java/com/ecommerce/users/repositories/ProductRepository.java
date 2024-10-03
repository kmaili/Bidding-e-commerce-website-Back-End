package com.ecommerce.users.repositories;

import com.ecommerce.users.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    Page<ProductEntity> findAllByOwner(Long userId, Pageable pageable);
    @Query("{ $and: [ { 'owner': ?1 }, { $or: [ { 'product_name': { $regex: ?0, $options: 'i' } }, { 'product_description': { $regex: ?0, $options: 'i' } } ] } ] }")
    Page<ProductEntity> findByOwnerAndSearchQuery(String query, Long userId, Pageable pageable);
}
