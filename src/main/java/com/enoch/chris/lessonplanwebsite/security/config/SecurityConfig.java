package com.enoch.chris.lessonplanwebsite.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.enoch.chris.lessonplanwebsite.service.UsersService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	    @Autowired
	    private UsersService usersService;
	
		@Autowired
		private DataSource securityDataSource;	
		
		@Autowired
		private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			
					auth.jdbcAuthentication().dataSource(securityDataSource).usersByUsernameQuery(
							"select username, password, enabled from User " +
							"where username=?")
							.authoritiesByUsernameQuery(
							"SELECT user.username, authority from user INNER JOIN user_roles"
							+ " ON user.id=user_id INNER JOIN role ON role_id=role.id WHERE user.username=?");

	
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.authorizeRequests()
				//.antMatchers("/").hasAnyRole("CUSTOMER", "MANAGER", "ADMIN")
				.antMatchers("/account/**").hasAnyRole("CUSTOMER", "MANAGER", "ADMIN")
				//.antMatchers("/admin/**", "/members/**").hasRole("ADMIN")
				.and()
				.formLogin()
					.loginPage("/showMyLoginPage")
					.loginProcessingUrl("/authenticateTheUser")
					.successHandler(customAuthenticationSuccessHandler)
					.permitAll()
				.and()
				.logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll().and()
				.exceptionHandling().accessDeniedPage("/error"); //don't show access-denied page as this would be a clue for hackers
			
			//http.csrf().disable();  //debugging
			
		}
		
		//beans
		//bcrypt bean definition
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		//authenticationProvider bean definition
		@Bean
		public DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
			auth.setUserDetailsService(usersService); //set the custom user details service
			auth.setPasswordEncoder(passwordEncoder()); //set the password encoder - bcrypt
			return auth;
		}
		
		@Bean
		public LogoutSuccessHandler logoutSuccessHandler() {
		    return new CustomLogoutSuccessHandler();
		}
			
	
}
