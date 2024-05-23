package com.scm.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScmApplication {
	public String PORT = System.getenv("PORT");
	public String DATABASE_URI = System.getenv("DATABASE_URI");


	public static void main(String[] args) {
		SpringApplication.run(ScmApplication.class, args);
	}

}
