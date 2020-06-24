package com.mess.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;
    @Autowired
    private DataSource dataSource;

    private final String USERS_QUERY = "select email, password, active from users where email=?";
    private final String ROLES_QUERY = "select u.email, r.role from users u inner join user_roles ur on (u.id = ur.user_id) inner join roles r on (ur.role_id=r.id) where u.email=?";
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(ROLES_QUERY)
                .dataSource(dataSource)
                .passwordEncoder(pbkdf2PasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home").permitAll()//home page permit all
                .antMatchers("/login").permitAll()//login page permit all
                .antMatchers("/signUp").permitAll()//sign up page permit all
                .antMatchers("/scripts/**").permitAll()//files in scripts permit all
                .antMatchers("/styles/**").permitAll()//files in styles permit all
                .antMatchers("/webjars/**").permitAll()//files in webjars permit all
                .antMatchers("/profile").hasAuthority("USER")//profile need authority with user role
                .anyRequest().authenticated().and().csrf().disable()//all else request need authentications
                .formLogin().loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/home")
                .usernameParameter("email").passwordParameter("password")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/access_denied")
                .and().httpBasic();
    }
}