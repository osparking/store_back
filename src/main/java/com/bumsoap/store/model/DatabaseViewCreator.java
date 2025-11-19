package com.bumsoap.store.model;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseViewCreator {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseViewCreator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void createViews() {
        String createViewSql = """
                CREATE OR REPLACE VIEW my_recipients AS 
                SELECT recipient.*
                FROM (
                    SELECT DISTINCT re.full_name, re.mb_phone, 
                        ab.zipcode, re.doro_zbun, ab.road_address, 
                        ab.z_bun_address, re.address_detail, bo.user_id
                    FROM recipient re
                    JOIN bs_order bo ON bo.recipient_id = re.id
                    JOIN address_basis ab ON ab.id = re.addr_basis_id
                    ORDER BY bo.order_time DESC
                ) AS recipient
                """;

        try {
            jdbcTemplate.execute(createViewSql);
            System.out.println("View 'my_recipients' created successfully");
        } catch (Exception e) {
            System.err.println("Failed to create view: " + e.getMessage());
        }
    }
}