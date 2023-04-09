package com.jpshoppingmall.auth.config;

import com.jpshoppingmall.auth.filter.CustomAuthenticationFilter;
import com.jpshoppingmall.auth.handler.LoginFailHandler;
import com.jpshoppingmall.type.EnumMaster.Role;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final LoginFailHandler loginFailHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/js/**", "/css/**", "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter =
            new CustomAuthenticationFilter(
                authenticationManager(authenticationConfiguration, authenticationManagerBuilder));
        customAuthenticationFilter.setFilterProcessesUrl(
            "/loginProc");  //  /loginProc 요청시 AuthenticationFilter 가서 처리를 합니다.
//        customAuthenticationFilter.setPostOnly(true); // 항상 POST 처리
        customAuthenticationFilter.setAuthenticationFailureHandler(
            loginFailHandler); // 성공시 핸들러 설정

        return http
            .httpBasic().disable()
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests()
            .antMatchers("/", "/login", "/js/**", "/css/**", "/static/**",
                "/swagger-ui/**",
                "/swagger-ui.html").permitAll()
            .antMatchers("/members/**", "/payment", "/products/{regex:\\d+}/review-form").authenticated()
            .antMatchers("/product-register")
            .hasAnyRole(Role.SELLER.name(), Role.ADMIN.name()) // 일반사용자 접근 가능
            // 나머지 요청에 대해서는 로그인을 요구하지 않음
            .anyRequest().permitAll()
            .and()
            /**
             * UsernamePasswordAuthenticationFilter 는
             * Spring Security 에서 폼 형식의 로그인 방식으로 작동할 때 사용할 수 있는 Filter 인데,
             * SecurityConfig 에 http.formLogin()라고 작성하면 자동으로 해당 필터를 사용하도록 등록된다.
             */
            .formLogin()
            .loginPage("/login")
            .successForwardUrl("/")
            .failureForwardUrl("/")
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
            .and()

            .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
        , AuthenticationManagerBuilder auth)
        throws Exception {
        auth.authenticationProvider(authenticationProvider); //authenticationProvider 를 설정합니다
        return authenticationConfiguration.getAuthenticationManager(); // AuthenticationManager 정보를 가져옵니다
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
