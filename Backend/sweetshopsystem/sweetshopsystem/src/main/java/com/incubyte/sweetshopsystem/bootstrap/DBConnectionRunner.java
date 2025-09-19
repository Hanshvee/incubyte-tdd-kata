package com.incubyte.sweetshopsystem.bootstrap;

import com.incubyte.sweetshopsystem.service.DBConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBConnectionRunner implements CommandLineRunner {

    private final DBConnectionService dbConnectionService;

    @Autowired
    public DBConnectionRunner(DBConnectionService dbConnectionService) {
        this.dbConnectionService = dbConnectionService;
    }

    @Override
    public void run(String... args) {
        dbConnectionService.checkConnection();
    }
}



