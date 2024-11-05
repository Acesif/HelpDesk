package com.grs.helpdeskmodule.config;

import com.grs.helpdeskmodule.jwt.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private final UserDetailsService userDetailsService;
    private final JWTFilter jwtFilter;

    private final String[] SUPERADMIN_PATHS = new String[]{
            "/api/auth/su/**",
    };
    private final String[] ADMIN_PATHS = new String[]{
            "/api/user/**",
            "/api/issue_reply/**",
            "/api/auth/all",
            "/api/dashboard/**"
    };
    private final String[] OFFICER_PATHS = new String[]{
            "/api/attachments",
            "/api/issue/user/**",
            "/api/issue/new"
    };
    private final String[] PERMITALL_PATHS = new String[]{
            "/api/user/create",
            "/api/user/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(requests ->
                requests
                        .requestMatchers(PERMITALL_PATHS).permitAll()
                        .requestMatchers(ADMIN_PATHS).hasAnyAuthority("ADMIN","SUPERADMIN")
                        .requestMatchers(SUPERADMIN_PATHS).hasAnyAuthority("SUPERADMIN","VENDOR")
                        .requestMatchers(OFFICER_PATHS).hasAnyAuthority("OFFICER","ADMIN","SUPERADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
