package com.lynhaw.g4test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class G4testApplication {
	@Autowired
	DataSource dataSource;
	public static void main(String[] args) {
		SpringApplication.run(G4testApplication.class, args);
	}

	public void run(String... args) throws Exception {
		System.out.println("DATASOURCE = " + dataSource);
	}
}
