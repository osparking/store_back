package com.bumsoap.store.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DelayedFlywayMigration {

    private final Flyway flyway;

    public DelayedFlywayMigration(Flyway flyway) {
        this.flyway = flyway;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runMigration() {
        // Option A: Delay using Thread.sleep (Blocking)
        try {
            Thread.sleep(5000); // Wait 5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Trigger the migration manually
        flyway.migrate();
    }
}
