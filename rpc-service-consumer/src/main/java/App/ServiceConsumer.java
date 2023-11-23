package App;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan(basePackages = {"spring.annotation","controller","spring.reference"})
@SpringBootApplication
public class ServiceConsumer {
    public static void main(String[] args) {
        SpringApplication.run(ServiceConsumer.class, args);
    }
}
