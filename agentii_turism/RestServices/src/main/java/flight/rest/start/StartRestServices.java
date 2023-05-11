package flight.rest.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
public class StartRestServices {
    public StartRestServices() {
    }

    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }
}