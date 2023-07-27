package com.example.userapi.configs;

import com.example.userapi.model.entity.User;
import com.example.userapi.model.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("!test")
@Log4j2
class InitiateDatabaseRecords {

   @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        if(repository.findAll().size()< 1) {
            return args -> {
                try {
                    repository.save(new User(null, "José Firmino", "jose@email.com", "565.378.540-75", 54));
                    repository.save(new User(null, "Maria Aparecida", "maria@email.com", "791.756.260-39", 32));
                    repository.save(new User(null, "João Vicente", "joao@email.com", "557.593.080-76", 60));
                    repository.save(new User(null, "Francisco Joaquim", "francisco@email.com", "242.959.900-78", 22));
                    repository.save(new User(null, "Juliana Silba", "juliana@email.com", "646.426.730-24", 54));

                    log.info("Registros iniciais inseridos no banco de dados." );
                } catch (Exception e) {
                    log.info("Erro ao iniciar registros no banco de dados." );
                }
            };
        }
        else
            return args -> {
                log.info("Registros já inseridos no banco de dados");
            };
    }




}