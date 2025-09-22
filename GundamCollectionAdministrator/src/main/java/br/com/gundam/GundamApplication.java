package br.com.gundam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class GundamApplication {

    public static void main(String[] args) {
        SpringApplication.run(GundamApplication.class, args);
    }

    @Bean
    ApplicationListener<ApplicationFailedEvent> applicationFailedLogger() {
        return event -> {
            Throwable cause = event.getException();
            int depth = 0;
            while (cause != null && depth < 15) {
                log.error("Startup failure (depth {}): {}: {}", depth, cause.getClass().getName(), cause.getMessage());
                cause = cause.getCause();
                depth++;
            }
            log.error("Hint: If the error mentions Flyway checksum mismatch, you likely changed a migration already applied. Options: 1) restore the original V1 file, 2) reset/repair the DB schema, or 3) bump a new migration V4 to alter schema.");
        };
    }
}
