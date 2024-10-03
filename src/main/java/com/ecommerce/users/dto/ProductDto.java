package com.ecommerce.users.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ProductDto {
     String id;
     String product_name;
     String product_description;
     List<String> product_images;
     float product_actual_price;
     int stock;
     private LocalDateTime created_at;
     Long owner;
}
