DROP VIEW IF EXISTS my_recipients;

CREATE VIEW my_recipients AS
SELECT recipient.*
FROM (
    SELECT DISTINCT re.full_name, re.mb_phone,
        ab.zipcode, re.doro_zbun, ab.road_address,
        ab.z_bun_address, re.address_detail, bo.user_id
    FROM recipient re
    JOIN bs_order bo ON bo.recipient_id = re.id
    JOIN address_basis ab ON ab.id = re.addr_basis_id
    ORDER BY bo.order_time DESC
) AS recipient;