package com.incubyte.sweetshopsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class DBConnectionService {

    private final DataSource dataSource;

    @Autowired
    public DBConnectionService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void checkConnection() {
        try (Connection conn = dataSource.getConnection()) {
            try (var stmt = conn.createStatement()) {
                var rs = stmt.executeQuery("SELECT COUNT(*) FROM sweets");
                if (rs.next()) {
                    System.out.println("Sweets table has " + rs.getInt(1) + " rows");
                }
            }
        } catch (Exception e) {
            System.out.println("DB query failed.");
            e.printStackTrace();
        }

    }
}

