package com.ecommerce.users.controllers;


import com.ecommerce.users.dto.UserDto;
import com.ecommerce.users.entities.UserEntity;
import com.ecommerce.users.mappers.UserMapper;
import com.ecommerce.users.repositories.UserRepository;
import com.ecommerce.users.services.JwtService;
import com.ecommerce.users.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Log log = LogFactory.getLog(UserController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/add")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        UserDto createdUserDto = userService.saveUser(userDto);
        return ResponseEntity.ok(createdUserDto);
    }

    @PatchMapping("/update")
    public ResponseEntity<Map<String, ?>> updateUser(@RequestBody Map<String, String> updatedField, HttpServletRequest request) {
        log.info(updatedField);
        Long userId = userIdFromToken(request);
        String field = updatedField.get("field");
        String value = updatedField.get("value");
        return ResponseEntity.ok(userService.updateUser(userId, field, value));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) {
        if (userService.deleteUser(userId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserDto> authenticate(@RequestBody Map<String, String> body, HttpServletResponse response) {
        try{
            String email = body.get("email");
            String password = body.get("password");

            UserDto userDto = userService.authenticate(email, password);
            final String jwtToken = jwtService.generateToken(userDto.getUser_id());
            Cookie cookie = new Cookie("JWT", jwtToken);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return ResponseEntity.ok(userDto);
        }catch (Exception e){
            log.error(e);
            return ResponseEntity.status(401).build();
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        System.out.println("JWT removed");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(HttpServletRequest request) {
        Long userId = userIdFromToken(request);
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        return ResponseEntity.ok(userMapper.toDto(userEntity));
    }

    private Long userIdFromToken(HttpServletRequest request){
        String jwtToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JWT")) {
                jwtToken = cookie.getValue();
                break;
            }
        }
        if (jwtToken == null)
            return null;
        return jwtService.extractUserId(jwtToken);
    }
}
