package server.inuappcenter.kr;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(servers =
                            {@Server(url = "https://server.inuappcenter.kr/", description = "Default Server URL"),
                                    @Server(url = "/", description = "Development Server URL")})
@SpringBootApplication
@EnableJpaAuditing
public class HomepageApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomepageApplication.class, args);
    }

}
