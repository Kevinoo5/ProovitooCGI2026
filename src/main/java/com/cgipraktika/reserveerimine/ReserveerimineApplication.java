package com.cgipraktika.reserveerimine;

import com.cgipraktika.reserveerimine.model.TableEntity;
import com.cgipraktika.reserveerimine.repository.TableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ReserveerimineApplication {

    private static final Logger logger = LoggerFactory.getLogger(ReserveerimineApplication.class);

    static void main(String[] args) {
        SpringApplication.run(ReserveerimineApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(TableRepository repository) {
        return (args) -> {
            repository.save(TableEntity.builder().seats(6).zone("Terrace").build());
            repository.save(TableEntity.builder().seats(4).zone("Private").isPrivate(true).build());
            repository.save(TableEntity.builder().seats(8).zone("Main Hall").hasWindow(true).build());

            logger.info("Tables found with findAll():");
            logger.info("-------------------------------");
            repository.findAll().forEach(table -> {
                logger.info(table.toString());
            });
            logger.info("");

            repository.findById(1L).ifPresent(table -> {
                logger.info("Table found with findById(1L):");
                logger.info("--------------------------------");
                logger.info(table.toString());
                logger.info("");
            });
        };
    }
}