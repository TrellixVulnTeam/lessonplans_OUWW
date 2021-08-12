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

import com.enoch.chris.lessonplanwebsite.controller.service.UsersService;

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

			/*
			 * // add users for in memory authentication
			
			UserBuilder users = User.withDefaultPasswordEncoder();
			
			auth.inMemoryAuthentication()
				.withUser(users.username("john").password("test123").roles("EMPLOYEE"))
				.withUser(users.username("mary").password("test123").roles("EMPLOYEE", "MANAGER"))
				.withUser(users.username("susan").password("test123").roles("EMPLOYEE", "ADMIN"));
			 */
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.authorizeRequests()
				.antMatchers("/").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
				.antMatchers("/leaders/**").hasRole("MANAGER")
				.antMatchers("/systems/**", "/members/**").hasRole("ADMIN")
				.antMatchers("/showProfilePage/**","/shop/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
				.and()
				.formLogin()
					.loginPage("/showMyLoginPage")
					.loginProcessingUrl("/authenticateTheUser")
					.successHandler(customAuthenticationSuccessHandler)
					.permitAll()
				.and()
				.logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll().and()
				.exceptionHandling().accessDeniedPage("/access-denied");
			
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
