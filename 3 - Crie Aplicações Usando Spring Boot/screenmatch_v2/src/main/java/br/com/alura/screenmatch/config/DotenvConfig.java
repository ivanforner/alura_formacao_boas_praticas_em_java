package br.com.alura.screenmatch.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {
    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();

//        dotenv.entries().forEach(e -> {
//            System.setProperty(e.getKey(), e.getValue());
//            System.out.println(e.getKey());
//        });
    }
}
