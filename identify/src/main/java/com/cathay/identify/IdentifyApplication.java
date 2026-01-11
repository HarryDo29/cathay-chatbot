package com.cathay.identify;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class IdentifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentifyApplication.class, args);
	}

	@Bean
	CommandLineRunner checkConnection(DataSource dataSource) {
		return args -> {
			try (Connection connection = dataSource.getConnection()) {
				if (connection.isValid(1000)) {
					System.out.println("✅ KẾT NỐI DATABASE THÀNH CÔNG! (DB: " + connection.getCatalog() + ")");
				} else {
					System.out.println("❌ KẾT NỐI THẤT BẠI!");
				}
			} catch (Exception e) {
				System.out.println("❌ LỖI KẾT NỐI: " + e.getMessage());
			}
		};
	}

}
