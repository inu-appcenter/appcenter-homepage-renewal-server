package server.inuappcenter.kr.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.AntPathMatcher;
import server.inuappcenter.kr.config.security.SecurityConfiguration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    static final String ADMIN = "[관리자]";
    static final String MEMBER = "[멤버]";
    static final String PUBLIC = "[공개]";

    @Bean
    public OpenAPI springOpenAPI() {
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
        SecurityRequirement securityItem = new SecurityRequirement().addList("bearerAuth");
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", bearerScheme))
                .addSecurityItem(securityItem)
                .info(new Info().title("Appcenter Homepage Application")
                        .description("앱센터 홈페이지 애플리케이션입니다.<br/><br/>" +
                                "각 API 제목 앞의 표시는 호출 권한입니다.<br/>" +
                                "<b>[공개]</b> 로그인 없이 호출 / " +
                                "<b>[멤버]</b> 로그인한 회원(관리자 포함) / " +
                                "<b>[관리자]</b> 관리자 계정만")
                        .version("0.5.0-SNAPSHOT"));
    }

    /**
     * API 하나를 문서로 만들 때마다 불린다.
     * 메서드(없으면 클래스)의 @PreAuthorize 를 읽어서 [관리자]/[멤버] 표시를 붙인다.
     */
    @Bean
    public OperationCustomizer roleBadgeCustomizer() {
        return (operation, handlerMethod) -> {
            PreAuthorize preAuthorize = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), PreAuthorize.class);
            if (preAuthorize == null) {
                preAuthorize = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), PreAuthorize.class);
            }
            if (preAuthorize != null) {
                String expression = preAuthorize.value();
                if (expression.contains("ADMIN")) {
                    addBadge(operation, ADMIN, "관리자 계정만 호출할 수 있습니다.");
                } else if (expression.contains("MEMBER")) {
                    addBadge(operation, MEMBER, "로그인한 회원(관리자 포함)이 호출할 수 있습니다.");
                }
            }
            return operation;
        };
    }

    /**
     * 문서가 다 만들어진 뒤 한 번 불린다.
     * 주소가 SecurityConfiguration 의 공개 목록에 있으면 [공개] 표시 + 자물쇠 제거,
     * @PreAuthorize 없이 로그인만 필요한 API 는 [멤버] 로 표시한다.
     */
    @Bean
    public OpenApiCustomiser publicApiCustomiser() {
        AntPathMatcher matcher = new AntPathMatcher();
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }
            openApi.getPaths().forEach((path, pathItem) -> {
                boolean isPublic = Arrays.stream(SecurityConfiguration.PERMIT_URI_ARRAY)
                        .anyMatch(pattern -> matcher.match(pattern, path));
                for (Operation operation : pathItem.readOperations()) {
                    if (hasBadge(operation)) {
                        continue;
                    }
                    if (isPublic) {
                        addBadge(operation, PUBLIC, "로그인 없이 호출할 수 있습니다.");
                        operation.setSecurity(new ArrayList<>()); // 전역 자물쇠 해제
                    } else {
                        addBadge(operation, MEMBER, "로그인한 회원(관리자 포함)이 호출할 수 있습니다.");
                    }
                }
            });
        };
    }

    private static boolean hasBadge(Operation operation) {
        String summary = operation.getSummary();
        return summary != null
                && (summary.startsWith(ADMIN) || summary.startsWith(MEMBER) || summary.startsWith(PUBLIC));
    }

    private static void addBadge(Operation operation, String badge, String text) {
        if (hasBadge(operation)) {
            return;
        }
        String summary = operation.getSummary();
        operation.setSummary(summary == null ? badge : badge + " " + summary);
        String description = operation.getDescription();
        String line = "<b>권한: " + badge + "</b> " + text;
        operation.setDescription(description == null || description.isEmpty() ? line : line + "<br/><br/>" + description);
    }
}
