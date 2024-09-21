package com.farmstory.config;

import com.farmstory.config.filter.CustomDateFilter;
import com.farmstory.service.user.UserScheduleService;
import com.farmstory.service.user.UserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserScheduleService userScheduleService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("admin") // 관리자만 접근 가능
                        .requestMatchers("/client/**").hasAnyRole("user","admin") // 일반 사용자만 접근 가능
                        .requestMatchers("/mypage/**").hasAnyRole("user","admin") //
                        .requestMatchers("/**","/error","/file/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new CustomDateFilter(userScheduleService), UsernamePasswordAuthenticationFilter.class)
                .formLogin(login -> login
                        .loginPage("/view/login")  // 로그인 페이지 URL
                        .loginProcessingUrl("/auth/user/login")  // 로그인 인증 처리 URL (디비 처리)
                        .defaultSuccessUrl("/")  // 로그인 성공 후 이동할 URL
                        .failureUrl("/view/login?error=true")  // 로그인 실패 시 이동할 URL
                        .usernameParameter("userName")  // 사용자명 파라미터 이름
                        .passwordParameter("pwd")  // 비밀번호 파라미터 이름
                        .permitAll()  // 로그인 페이지는 인증 없이 접근 가능
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/user/logout","GET"))  // 절대 경로로 설정
                        .logoutSuccessUrl("/"))
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);


        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }



}
