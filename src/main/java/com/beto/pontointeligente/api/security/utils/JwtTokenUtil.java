package com.beto.api.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_ROLE = "role";
	static final String CLAIM_KEY_CREATED = "created";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;

	public JwtTokenUtil() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Obtem o username (email) contido no token
	 * 
	 * @param token
	 * @return
	 */
	public String getUserNameFromToken(String token) {
		String username;
		try {
			Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			// TODO: handle exception
			username = null;
		}
		return username;
	}
	
	/**
	 * Retorna a data de expiracao do token
	 * 
	 * @param token
	 * @return
	 */
	public Date getGetExpirationDateFromToken(String token){
		Date expiration;
		
		try {
			Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			// TODO: handle exception
			expiration = null;
		}
		return expiration;
	}	

	/**
	 * Cria um token novo (refresh)
	 * 
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = gerarToken(claims);
		} catch (Exception e) {
			// TODO: handle exception
			refreshedToken = null;
		}
  		return refreshedToken;
	}
	
	/**
	 * Realiza o parse do token JWT para extrair as informcoes contidas no corpo dele. 
	 * 
	 * @param token
	 * @return Claims
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			claims = null;
		}
		
		return claims;
	}
	
	/**
	 * Verifica e retorna um JWT valido
	 * 
	 * @param token
	 * @return boolean
	 */
	public boolean tokenValido(String token) {
		return !tokenExpirado(token);
	}
	
	public String obterToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		userDetails.getAuthorities().forEach(authotiry -> claims.put(CLAIM_KEY_ROLE, authotiry.getAuthority()));
		claims.put(CLAIM_KEY_CREATED, new Date());
		
		return gerarToken(claims);
		
	}

	/**
	 * Retorna a data de expiracao com base na data atual.
	 * @return Date
	 */
	private Date gerarDataDeExpiracao () {
		return new Date(System.currentTimeMillis() + expiration + 1000); 
	}
	
	/**
	 * Verifica se o token esta expirado
	 * @param token
	 * @return boolean
	 */
	private boolean tokenExpirado(String token) {
		Date dataExpiracao = this.getGetExpirationDateFromToken(token);
		if(dataExpiracao == null) {
			return false;
		}
		return dataExpiracao.before(new Date());
	}
	
	private String gerarToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(gerarDataDeExpiracao())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
