package spaceflight.config;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("Dominik").password("dominik123").roles("ADMIN").and()
                .withUser("Darek").password("darek123").roles("MANAGER").and()
                .withUser("Joanna").password("joanna123").roles("EMPLOYEE");
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

       http.csrf().disable()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .authorizeRequests()
               .antMatchers(HttpMethod.POST, "/login").permitAll()
               .antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "EMPLOYEE", "MANAGER")
               .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "EMPLOYEE", "MANAGER")
               .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN", "MANAGER")
               .antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "EMPLOYEE", "MANAGER")
               .anyRequest().authenticated();


        http.csrf().disable();
    }

}
