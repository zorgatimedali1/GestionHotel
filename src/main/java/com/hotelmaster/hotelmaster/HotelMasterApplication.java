package com.hotelmaster.hotelmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * ✅ Point d'entrée de l'application.
 *
 * Étend SpringBootServletInitializer pour permettre le déploiement
 * sur un serveur Tomcat externe (apache-tomcat-9.0.115).
 *
 * Sans cette extension, Tomcat ne saurait pas comment démarrer l'application.
 */
@SpringBootApplication
public class HotelMasterApplication extends SpringBootServletInitializer {

    /**
     * Appelé par Tomcat externe lors du déploiement du WAR.
     * Configure le point d'entrée Spring Boot.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HotelMasterApplication.class);
    }

    /**
     * Utilisé uniquement si on lance le projet directement via Maven
     * (mvn spring-boot:run) — pas nécessaire avec Tomcat externe.
     */
    public static void main(String[] args) {
        SpringApplication.run(HotelMasterApplication.class, args);
    }
}
