package spaceflight.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spaceflight.service.UserDetailServiceImpl;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailServiceImpl userDetailService;

    @Autowired
    public WebSecurityConfig(UserDetailServiceImpl userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
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
               .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN")
               .antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN", "EMPLOYEE", "MANAGER")
               .anyRequest().authenticated();

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
