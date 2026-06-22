package com.mariluz.reportes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
        "jwt.secret=98PAzqkLUAElxtt+iNCg3GxPvqHI8ytzNfilW2S3RhQ=",
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
    }
)
class ReportesApplicationTests {

    @Test
    void contextLoads() {}
}
