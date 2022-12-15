package com.sparta.actualpractice.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class JDBCTest {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Test
    public void testMysqlConnect() {

        try(Connection connection =
                    DriverManager.getConnection(
                            url,
                            userName,
                            password
                    )) {
            System.out.println(connection + " connecting success" );
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
