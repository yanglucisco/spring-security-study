package org.yanglu.spring.security.study.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class ResourceServerConfig {
    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    public ResourceServerConfig(CustomJwtAuthenticationConverter customJwtAuthenticationConverter) {
        this.customJwtAuthenticationConverter = customJwtAuthenticationConverter;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt->jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)))
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/resource1/testscope").hasAuthority("SCOPE_articles.read")
                .requestMatchers("/resource1/testroleadmin").hasRole("admin")
                .requestMatchers("/resource1/testrolenormal").hasRole("normal")
                .anyRequest().authenticated()
        )
        ;
        return http.build();
    }

    // @Bean
	// public JwtAuthenticationConverter jwtAuthenticationConverter() {
	// 	var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
	// 	jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
	// 	jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

	// 	var jwtAuthenticationConverter = new JwtAuthenticationConverter();
	// 	jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
	// 	return jwtAuthenticationConverter;
	// }
}
