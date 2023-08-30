package home.inuappcenter.kr.appcenterhomepagerenewalserver.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Appcenter Homepage Application")
                        .description("앱센터 홈페이지 애플리케이션\n서버를 잘 확인하고 사용해주세요")
                        .version("0.1.5-SNAPSHOT"));
    }
}
