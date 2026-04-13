package com.example.apa_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class ApaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApaProjectApplication.class, args);
    }

    @Bean
    CommandLineRunner openHomePageOnStartup(@Value("${server.port:8080}") String serverPort) {
        return args -> {
            String url = "http://localhost:" + serverPort + "/";
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ignored) {
                    // Ignore browser-launch failures so app startup is not interrupted.
                }
            }
        };
    }
}

