package App;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"spring.annotation","spring.service","service"})
@SpringBootApplication
public class ServiceProvider {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServiceProvider.class, args);
    }
}
