package fr.diginamic.webmvc01;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		/**
		 * ATTTENTION JE DESACTIVE SPRINGBOOTSECURITY
		 * 	*http.csrf().disable().authorizeRequests().anyRequest().permitAll();
		 * 
		 * ATTENTION PLUS AUCUN ACCES AU SITE
		 *	*http.csrf().disable().authorizeRequests().anyRequest().authenticated();
		 */
		
		http.csrf().disable()
        .formLogin().loginProcessingUrl("/login").and()
        .logout().logoutUrl("/logout").invalidateHttpSession(true).and()
        .authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/logout").permitAll()
        .anyRequest().authenticated().and().httpBasic();
		
		/**
		 * httpBasic() -> API REST avec leur propre sécurité
		 */
		
		
	}
}
