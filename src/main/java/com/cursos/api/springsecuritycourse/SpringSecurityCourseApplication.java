package com.cursos.api.springsecuritycourse;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSecurityCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityCourseApplication.class, args);
	}

	@Bean
	public CommandLineRunner createPasswordsCommand(PasswordEncoder passwordEncoder){
		return args -> {

			System.out.println(passwordEncoder.encode("clave123"));
			System.out.println(passwordEncoder.encode("clave456"));
			System.out.println(passwordEncoder.encode("clave789"));

		};
	}

}
