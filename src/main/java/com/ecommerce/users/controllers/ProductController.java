package com.ecommerce.users.controllers;


import com.ecommerce.users.dto.ProductDto;
import com.ecommerce.users.services.ProductService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Log log = LogFactory.getLog(ProductController.class);
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        ProductDto savedProduct = productService.addProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        log.info("coming product"+ productDto);
        ProductDto savedProduct = productService.updateProduct(productDto);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/delete/{product_id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable String product_id) {
        if (productService.deleteProduct(product_id)){
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/load_user_products")
    public ResponseEntity<Page<ProductDto>> loadUserProducts(@RequestParam String owner,
                                                             @RequestParam int pageIndex,
                                                             @RequestParam int pageSize,
                                                             @RequestParam String sortBy,
                                                             @RequestParam String sortOrder,
                                                             @RequestParam String searchQuery
                                                             ) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize,
                (sortOrder.equals("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending());
        if (searchQuery != null && !searchQuery.isEmpty()){
            return ResponseEntity.ok(productService.getUserProductsByPage(Long.parseLong(owner), searchQuery, pageable));
        }else{
            return ResponseEntity.ok(productService.getUserProductsByPage(Long.parseLong(owner), pageable));
        }
    }
}
