package com.ecommerce.users.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDto {
    private Long user_id;
    private String username;
    private String first_name;
    private String last_name;
    private String birth;
    private String phone;
    private String email;
    private String password;
    private String profile_image;
}
