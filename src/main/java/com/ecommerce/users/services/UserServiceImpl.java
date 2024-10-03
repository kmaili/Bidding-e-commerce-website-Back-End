package com.ecommerce.users.services;

import com.ecommerce.users.dto.UserDto;
import com.ecommerce.users.entities.UserEntity;
import com.ecommerce.users.mappers.UserMapper;
import com.ecommerce.users.repositories.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final Log log = LogFactory.getLog(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;



    private String hash_salt = BCrypt.gensalt(10);

    @Override
    public UserDto saveUser(UserDto user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), hash_salt);
        user.setPassword(hashedPassword);
        UserEntity userEntity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDto(userEntity);
    }

    @Override
    public Map<String, ?> updateUser(Long userId, String field, String value) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (Objects.equals(field, "password")) {
            String hashedPassword = BCrypt.hashpw(value, hash_salt);
            user.setPassword(hashedPassword);
        }else{
            try {
                log.info("\n\n"+field+"\n\n");
                Field ClassField = UserEntity.class.getDeclaredField(field);
                ClassField.setAccessible(true);
                ClassField.set(user, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        userRepository.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("field", field);
        response.put("newValue", value);
        return response;
    }

    @Override
    public boolean deleteUser(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;

    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        UserDto user = userMapper.toDto(userEntity);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new LinkedList<>()
        );
    }

    @Override
    public UserDto authenticate(String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, hash_salt);
        UserEntity userEntity = userRepository.findByEmailAndPassword(email, hashedPassword);
        log.info(email+" : "+hashedPassword +" : " + password);
        return userMapper.toDto(userEntity);
    }
}
