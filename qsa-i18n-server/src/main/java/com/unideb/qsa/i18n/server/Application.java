package com.unideb.qsa.i18n.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Server.
 */
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.unideb.qsa")
public class Application {

    /**
     * Main entry point to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}


