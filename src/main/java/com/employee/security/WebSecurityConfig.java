package com.employee.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
    return security.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> request.requestMatchers("/team/**","/emp/**").hasAnyAuthority("MANAGER","SOFTWARE_ENGINEER"))
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
    authProvider.setUserDetailsService(userDetailsService);
    return authProvider;
  }


//  @Bean
//  public UserDetailsService userDetailsService(){
//
//    UserDetails user1 = User.withDefaultPasswordEncoder().username("name").password("paswd").roles("USER").build();
//    UserDetails user2 = User.withDefaultPasswordEncoder().username("name1").password("paswd1").roles("ADMIN").build();
//
//    return new InMemoryUserDetailsManager(user1,user2);
//  }

}
