package server.inuappcenter.kr.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import server.inuappcenter.kr.config.security.exception.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
    public class SecurityConfiguration {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] PERMIT_URI_ARRAY = {
            "/groups/public/*",
            "/faqs/public/*",
            "/introduction-board/public/*",
            "/photo-board/public/*",
            "/activity-board/public/*",
            "/stacks/public/*",
            "/recruitment/public/*",
            "/recruitment-fields/public/*",
            "/image/photo/*",
            "/sign/sign-in",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(PERMIT_URI_ARRAY).permitAll()
                .antMatchers(HttpMethod.GET, "*").permitAll()
                .antMatchers("**exception**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .cors();
        return httpSecurity.build();
    }


}
