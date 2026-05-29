package br.com.cotiinformatica.api_agenda.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtTokenComponent {

    @Value("${jwt.secret}")
    private String secret;

    /*
        Método para extrair o ID do usuário contido no TOKEN
     */
    public UUID getUserId(HttpServletRequest http) {

        try {
            String authorization = http.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return null;
            }

            String token = authorization.substring(7);

            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            String user = claims.getSubject();

            return UUID.fromString(user);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
