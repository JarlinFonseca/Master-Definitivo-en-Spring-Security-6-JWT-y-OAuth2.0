package com.cursos.api.springsecuritycourse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSecurityCourseApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityCourseApplication.class);
	@Value("${spring.security.password1}")
	private String password1;
	@Value("${spring.security.password2}")
	private String password2;
	@Value("${spring.security.password3}")
	private String password3;
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityCourseApplication.class, args);
	}

	@Bean
	public CommandLineRunner createPasswordsCommand(PasswordEncoder passwordEncoder){
		return args -> {

			String pass1 = (passwordEncoder.encode(password1));
			String pass2 = (passwordEncoder.encode(password2));
			String pass3 = (passwordEncoder.encode(password3));



			LOGGER.info(pass1);
			LOGGER.info(pass2);
			LOGGER.info(pass3);
		};
	}

}
