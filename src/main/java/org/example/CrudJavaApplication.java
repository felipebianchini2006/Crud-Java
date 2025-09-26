package org.example;

import java.awt.Desktop;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("org.example.library")
@EnableJpaRepositories("org.example.library")
@EnableJpaAuditing
public class CrudJavaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrudJavaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CrudJavaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void launchBrowserAfterStartup() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI("http://localhost:8080/"));
                }
            }
        } catch (Exception ex) {
            LOGGER.warn("Não foi possível abrir o navegador automaticamente.", ex);
        }
    }
}