package server.inuappcenter.kr.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springOpenAPI() {
        SecurityScheme apikey = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name("X-AUTH-TOKEN");
        SecurityRequirement securityItem = new SecurityRequirement().addList("basicAuth");
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("basicAuth", apikey))
                .addSecurityItem(securityItem)
                .info(new Info().title("Appcenter Homepage Application")
                        .description("앱센터 홈페이지 애플리케이션입니다.")
                        .version("0.5.0-SNAPSHOT"));
    }


}
