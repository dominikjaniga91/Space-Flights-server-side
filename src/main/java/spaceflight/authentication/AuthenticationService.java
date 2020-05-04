package spaceflight.authentication;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import spaceflight.model.User;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class AuthenticationService {

    static final long EXPIRATION_TIME = 86_400_000;
    static final String SIGNING_KEY = "SecretKey";
    static final String PREFIX = "Bearer";
    static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    static public void addToken(HttpServletResponse response, User user){
        String JwtToken = Jwts.builder().setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();
        response.addHeader("Authorization", PREFIX + JwtToken);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

    }


    static public Authentication getAuthentication(ServletRequest request, ServletResponse response) throws IOException {

        String token = ((HttpServletRequest)request).getHeader("Authorization");

        try{
            if( token != null){

                Claims claims  = Jwts.parser()
                        .setSigningKey(SIGNING_KEY)
                        .parseClaimsJws(token.replace(PREFIX, ""))
                        .getBody();

                String role = "ROLE_" + claims.get("role").toString();
                String username = claims.getSubject();

                if(username != null)
                    return new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(new SimpleGrantedAuthority(role)));
            }

        } catch (ExpiredJwtException exception) {
            ((HttpServletResponse) response).setHeader("expired" ,exception.getMessage());
            logger.warn("Request to parse expired JWT : {} \n failed : {}", token, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            logger.warn("Request to parse unsupported JWT : {} \n failed : {}", token, exception.getMessage());
        } catch (MalformedJwtException exception) {
            logger.warn("Request to parse invalid JWT : {} \n failed : {}", token, exception.getMessage());
        } catch (SignatureException exception) {
            logger.warn("Request to parse JWT with invalid signature : {} \n failed : {}", token, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            logger.warn("Request to parse empty or null JWT : {} \n failed : {}", token, exception.getMessage());
        }
        return null;
    }

}
