package no.charlie.rsvp.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    String noUser = 'noUser'
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String username = System.getenv("ADMIN_USERNAME")
        String password = System.getenv("ADMIN_PASS")
        auth.inMemoryAuthentication()
                .withUser(username?:noUser).password(password?:noUser).roles('KingKenny')
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .antMatcher('/api/admin/**').authorizeRequests().anyRequest().hasRole('KingKenny').and()
                .httpBasic()
                .and()
                .csrf().disable();

    }

}
