package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Bean //Para saber que este metodo sobrescreve e retorna o authenticantioManager
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	
	//Configurações de Autenticação, controle de acesso, login
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configurações de autorização, perfil de acesso, liberação de url
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll() //tudo que for relacionado com a url /topicos esta liberado, com o httpMethod, somente o Get é liberado para todos
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
			.antMatchers(HttpMethod.POST, "/auth").permitAll()
			.anyRequest().authenticated()
			.and().csrf().disable() //csrf() cross-site request forgery é desabilitado pq a autenticação de forma stateless
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
//			.and().formLogin(); //Informa para o Spring gerar um formulario de autenticação, formLogin cria seções
			
	}
	
	//Configurações de recursos estáticos(acesso a arquivos js, css, imagens, etc).
	@Override
	public void configure(WebSecurity web) throws Exception {
	}
	
//		System.out.println(new BCryptPasswordEncoder().encode("123456"));
}	
