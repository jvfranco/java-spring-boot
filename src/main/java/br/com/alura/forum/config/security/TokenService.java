package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}") //anotação que pega uma propriedade no arquivo application.properties
	private String expiration;
	
	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("API do Fórum da Alura") // quem solicitou o token
				.setSubject(logado.getId().toString()) //id do usuario
				.setIssuedAt(hoje) //data da solicitacao
				.setExpiration(dataExpiracao) //data da expiracao
				.signWith(SignatureAlgorithm.HS256, secret) //algoritmo de criptografia e chave
				.compact();
	}

	public boolean isTokenValido(String token) {
		try {
			//BibliotecaJwts, parser para descriptografar, chave da criptografia, parseClaim retorna o token se estiver valido senao retorna uma exception
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token); 
			return true;
		}
		catch (Exception e) {
			return false;
		}
		
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long .parseLong(claims.getSubject());
	}
	
}
