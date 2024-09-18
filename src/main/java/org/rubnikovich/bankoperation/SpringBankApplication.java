package org.rubnikovich.bankoperation;

import org.modelmapper.ModelMapper;
import org.rubnikovich.bankoperation.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBankApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}