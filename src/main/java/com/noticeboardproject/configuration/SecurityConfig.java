package com.noticeboardproject.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private LogoutSuccessHandler customLogoutSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/h2-console/**", "/user/registration", "/user/successRegister", "/user/badToken", "/user/badUser", "/user/emailError", "/user/forgotPassword", "/login", "/index", "/")
				.permitAll()
				.antMatchers("/user/updatePassword*", "/user/savePassword*", "/user/changePasswordForLoggedUser/*")
				.hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
				.antMatchers("/addNewNotice")
				.authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/")
				.failureUrl("/login?error=true")
				.permitAll()
			.and()
			.logout()
				.logoutSuccessHandler(customLogoutSuccessHandler)
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll();
		//prevent blank page after logging into the H2 console
		http.headers().frameOptions().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
	  throws Exception {
		auth.authenticationProvider(authProvider());
	}
	
	@Bean
    public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
	}
}
