package com.wangdm.tutorial.springsecurity.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Autowired
	UserDetailsService customUserDetailsService;
	@Autowired
	PasswordEncoder customPasswordEncoder;

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		log.debug("authenticationManager");
		return super.authenticationManager();
	}

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		log.debug("userDetailsService");
		return customUserDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.debug("configure(AuthenticationManagerBuilder auth)");
		auth.userDetailsService(customUserDetailsService).passwordEncoder(customPasswordEncoder);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		log.debug("configure(WebSecurity web)");
		super.configure(web);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.debug("configure(HttpSecurity http)");
		http
		.authorizeRequests().antMatchers("/assets/**").permitAll()
		.anyRequest().authenticated().and()
		.formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll().and()
		.logout().logoutSuccessUrl("/login").permitAll();
		super.configure(http);
	}
	
}
