package devocean.tickit.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private String apiVersion = "/api/v1";

    private String[] attendeeList = {
            "/user", "/users/**",
            "/events", "/events/**",
            "/wallets", "/wallets/**",
            "/albums", "/albums/**",
            "/tickets", "/tickets/**",
            "/mypage", "/mypage/**",

    };

    private String[] hostList = {
            "/participants", "/participants/**",
    };

    public String[] getVersionedList(String[] apiList) {
        return Arrays.stream(apiList)
                .map(path -> apiVersion + path)
                .toArray(String[]::new);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> {
                    auth
                            .requestMatchers(getVersionedList(attendeeList)).permitAll()
                            .requestMatchers(getVersionedList(hostList)).permitAll()
                            // 역할 검증이 필요한 uri 추가하기 .permit
                            .anyRequest().authenticated();
                })
                // jwtFilter를 UsernamePasswordAuthenticationFilter 앞에 추가할 것

        ;
        return http.build();
    }
}
