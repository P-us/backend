package projectus.pus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class pusapplication {
	public static void main(String[] args) {
		SpringApplication.run(pusapplication.class, args);
	}
}
