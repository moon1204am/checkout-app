package se.kth.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KlarnaBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(KlarnaBackendApplication.class, args);
	}

}
