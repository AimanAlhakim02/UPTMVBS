package com.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbConnectionTester implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            System.out.println("Database Connection Successful");
        } catch (Exception e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
        }
    }
}
