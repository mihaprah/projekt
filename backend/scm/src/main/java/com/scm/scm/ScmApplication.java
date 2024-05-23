package com.scm.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScmApplication {
	public String PORT = System.getenv("PORT");
	public String DATABASE_URL = System.getenv("DATABASE_URL");
	public String DATABASE_NAME = System.getenv("DATABASE_NAME");
	public String DATABASE_USERNAME = System.getenv("DATABASE_USERNAME");
	public String DATABASE_PASSWORD = System.getenv("DATABASE_PASSWORD");
	public String DATABASE_PORT = System.getenv("DATABASE_PORT");


	public static void main(String[] args) {
		SpringApplication.run(ScmApplication.class, args);
	}

}
