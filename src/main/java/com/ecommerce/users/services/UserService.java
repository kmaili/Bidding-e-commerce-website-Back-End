package com.ecommerce.users.services;

import com.ecommerce.users.dto.UserDto;
import com.ecommerce.users.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface UserService {
   UserDto saveUser(UserDto user);
   Map<String, ?> updateUser(Long userId, String field, String value);
   boolean deleteUser(long id);
   UserDetails loadUserByUserId(Long userId);
   UserDto authenticate(String email, String password);
}
