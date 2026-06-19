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

    @EventListener(ContextRefreshedEvent.class)
    public void createDeleteCustomerByIdProcedure() {
        String dropSql =
                "DROP PROCEDURE IF EXISTS delete_customer_by_id";
        String createSql =
                """
                CREATE PROCEDURE bs_store.delete_customer_by_id(IN uid INT)
                BEGIN
                    START TRANSACTION;
                
                    -- 1. user_roles 테이블에서 해당 사용자와 연결된 행 삭제
                    DELETE FROM user_roles
                    WHERE user_id = uid;
                
                    -- 2. verification_token 테이블에서 해당 사용자와 연결된 행 삭제
                    DELETE FROM verifin_token
                    WHERE user_id = uid;
                
                    -- 3. customer 테이블에서 해당 사용자와 연결된 행 삭제
                    DELETE FROM customer
                    WHERE customer_id = uid;
                
                    -- 4. bs_user 테이블에서 해당 사용자 삭제
                    DELETE FROM bs_user
                    WHERE id = uid;
                
                    -- 모든 삭제 성공 시 커밋
                    COMMIT;
                END
                """;
        try {
            jdbcTemplate.execute(dropSql);
            jdbcTemplate.execute(createSql);
            System.out.println("프로시저 'delete_customer_by_id' 생성 성공");
        } catch (Exception e) {
            System.err.println("생성이 실패된 프로시져 : " + e.getMessage());
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void createDeleteWorkerByIdProcedure() {
        String dropSql =
                "DROP PROCEDURE IF EXISTS delete_customer_by_id";
        String createSql =
                """
                CREATE DEFINER=`root`@`localhost`
                    PROCEDURE `bs_store`.`delete_worker_by_id`(IN uid INT)
                BEGIN
                    START TRANSACTION;
                
                    -- 1. user_roles 테이블에서 해당 사용자와 연결된 행 삭제
                    DELETE FROM user_roles
                    WHERE user_id = uid;
                
                    -- 2. verification_token 테이블에서 해당 사용자와 연결된 행 삭제
                    DELETE FROM verifin_token
                    WHERE user_id = uid;
                
                    -- 3. customer 테이블에서 해당 사용자와 연결된 행 삭제
                    DELETE FROM worker
                    WHERE worker_id = uid;
                
                    -- 4. customer 테이블에서 해당 사용자와 연결된 행 삭제
                    DELETE FROM employee
                    WHERE employee_id = uid;

                    -- 5. bs_user 테이블에서 해당 사용자 삭제
                    DELETE FROM bs_user
                    WHERE id = uid;
                
                    -- 모든 삭제 성공 시 커밋
                    COMMIT;
                END
                """;
        try {
            jdbcTemplate.execute(dropSql);
            jdbcTemplate.execute(createSql);
            System.out.println("프로시저 'delete_customer_by_id' 생성 성공");
        } catch (Exception e) {
            System.err.println("생성이 실패된 프로시져 : " + e.getMessage());
        }
    }
}