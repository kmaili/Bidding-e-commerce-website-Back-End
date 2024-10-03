package com.ecommerce.users;

import com.ecommerce.users.entities.UserEntity;
import com.ecommerce.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsersApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args){
		SpringApplication.run(UsersApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
