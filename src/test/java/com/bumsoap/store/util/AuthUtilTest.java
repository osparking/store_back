package com.bumsoap.store.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AuthUtilTest {
    String WEBHOOK_SECRET = "2e55b9a3-4dd1-4416-9897-c4bd1e3d738f";
    String timestamp = "1662371528";
    String expect =
            "a37084ab68ae16b77db1f8463f31be9fcc965e2515e03efecf8139bb1e511b06";
    @Test
    void generateHmacSHA256() throws Exception {
        String result = AuthUtil.generateSignature(timestamp, WEBHOOK_SECRET);
        Assertions.assertEquals(result, expect, "서명 동일");
    }
}
