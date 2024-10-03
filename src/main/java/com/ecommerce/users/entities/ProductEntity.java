package com.ecommerce.users.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Getter
@Setter
@Document(collection = "products")
public class ProductEntity {

    @Id
    private String id;
    private String product_name;
    private String product_description;
    private List<String> product_images;
    private float product_actual_price;
    private int stock;
    @CreatedDate
    private LocalDateTime created_at;
    @Indexed
    private Long owner;

}
