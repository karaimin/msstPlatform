package com.msst.platform.config;

import com.msst.platform.security.AuthoritiesConstants;
import com.msst.platform.security.jwt.JWTFilter;
import com.msst.platform.security.jwt.TokenProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration {

    private final ReactiveUserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final CorsWebFilter corsWebFilter;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(ReactiveUserDetailsService userDetailsService, TokenProvider tokenProvider, CorsWebFilter corsWebFilter, SecurityProblemSupport problemSupport) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.corsWebFilter = corsWebFilter;
        this.problemSupport = problemSupport;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .addFilterAt(corsWebFilter, SecurityWebFiltersOrder.HTTP_HEADERS_WRITER)
            .addFilterAt(new JWTFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
            .addFilterAt(new AuthenticationWebFilter(reactiveAuthenticationManager()), SecurityWebFiltersOrder.AUTHENTICATION)
            .exceptionHandling()
            .accessDeniedHandler(problemSupport)
            .authenticationEntryPoint(problemSupport)
        .and()
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .pathMatchers("/*").permitAll()
            .pathMatchers("/app/**/*.js").permitAll()
            .pathMatchers("/app/**/*.html").permitAll()
            .pathMatchers("/i18n/**").permitAll()
            .pathMatchers("/content/**").permitAll()
            .pathMatchers("/swagger-ui/index.html").permitAll()
            .pathMatchers("/test/**").permitAll()
            .pathMatchers("/api/register").permitAll()
            .pathMatchers("/api/subtitles/pending/*").permitAll()
            .pathMatchers("/api/subtitles/translate/*").permitAll()
            .pathMatchers("/api/activate").permitAll()
            .pathMatchers("/api/authenticate").permitAll()
            .pathMatchers("/api/account/reset-password/init").permitAll()
            .pathMatchers("/api/account/reset-password/finish").permitAll()
            .pathMatchers("/api/**").authenticated()
            .pathMatchers("/management/health").permitAll()
            .pathMatchers("/management/info").permitAll()
            .pathMatchers("/management/prometheus").permitAll()
            .pathMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN);
        return http.build();
    }
}
