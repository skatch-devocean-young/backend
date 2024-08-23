package devocean.tickit.global.config;

import devocean.tickit.global.jwt.JwtFilter;
import devocean.tickit.global.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtils jwtUtils;
    private String apiVersion = "/api/v1";
    private String[] attendeeList = {
            "/", "/**",
            "/health",
            "/test", "/test/**",
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


    // CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
//                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> {
                    auth
                            .requestMatchers("/**").permitAll()
                            .requestMatchers(getVersionedList(attendeeList)).permitAll()
                            .requestMatchers(getVersionedList(hostList)).permitAll()
                            .anyRequest().authenticated()
                    ;
                })
//                .addFilterBefore(new JwtFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)

        ;
        return http.build();
    }
}