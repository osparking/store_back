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

    @EventListener(ContextRefreshedEvent.class)
    public void createOrphanRecipientDeleteProcedure() {
        String dropSql =
                "DROP PROCEDURE IF EXISTS safe_delete_recipient";
        String createSql =
                """
                CREATE PROCEDURE bs_store.safe_delete_recipient(IN re_id INT)
                BEGIN
                    DECLARE order_exists INT;
                    -- 주문 존재 여부 확인
                    SELECT COUNT(*) INTO order_exists
                    FROM bs_order
                    WHERE recipient_id = re_id;
                    -- 주문이 없을 때만 삭제
                    IF order_exists = 0 THEN
                        DELETE FROM recipient WHERE id = re_id;
                        SELECT CONCAT('삭제된 고아 수신처 ID : ', re_id) AS message;
                    ELSE
                        SELECT CONCAT('유지 필요한 수신처 ID : ', re_id) AS message;
                    END IF;
                END
                """;
        try {
            jdbcTemplate.execute(dropSql);
            jdbcTemplate.execute(createSql);
            System.out.println("프로시저 'safe_delete_recipient' 생성 성공");
        } catch (Exception e) {
            System.err.println("생성이 실패된 프로시져 : " + e.getMessage());
        }
    }
}