package com.fiera.api.configurations;

import com.fiera.api.models.User;
import com.fiera.api.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(UserRepository repository) {

    User user = new User();
    user.setDocNumber("123456789");
    user.setFirstName("Luke");
    user.setLastName("Skywalker");
    user.setEmail("jedi@email.com");
    user.setPhone("987654321");
    user.setAddress("123 death star avenue");
    user.setSecretField("SecretField");

    return args -> {
      log.info("Preloading " + repository.save(user));
    };
  }
}