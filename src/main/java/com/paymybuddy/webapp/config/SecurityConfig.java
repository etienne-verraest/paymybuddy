package com.paymybuddy.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.paymybuddy.webapp.service.UserService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Defining the passwordEncoder here, to avoid plain-text manpulations
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests() //
				.antMatchers("/css/**").permitAll() // Allow CSS to be loaded
				.antMatchers("/user").hasRole("USER") // Only allow logged user for this page
				.antMatchers("/login", "/register").permitAll() // Permit anonymous users to access these pages
				.anyRequest().authenticated() // Every others pages must be accessed with valid credentials
				.and() //
				.formLogin().loginPage("/login").defaultSuccessUrl("/user", true) // Login form parameters
				.usernameParameter("mail") //
				.and() //
				.logout().logoutUrl("/logout").invalidateHttpSession(true) // Logout parameters
				.deleteCookies("JSESSIONID") // Delete cookies on logout
				.and().csrf().disable(); // Disabling CSRF Tokens
	}
}
