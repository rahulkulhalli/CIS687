package org.syr.cis687;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Cis687Application {
	public static void main(String[] args) {
		SpringApplication.run(Cis687Application.class, args);
	}
}
