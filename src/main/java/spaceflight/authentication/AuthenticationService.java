package spaceflight.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

public class AuthenticationService {

    static final long EXPIRATION_TIME = 86_400_000;
    static final String SIGNING_KEY = "SecretKey";
    static final String PREFIX = "Bearer";

    static public void addToken(HttpServletResponse response, String username){
        String JwtToken = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();

        response.addHeader("Authorization", PREFIX + " " + JwtToken);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }


    static public Authentication getAuthentication(HttpServletRequest request){

        String token = request.getHeader("Authorization");
        System.out.println("token: " + token );
        if( token != null){
            String user = Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody()
                    .getSubject();

            if(user != null)
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        }
        return null;
    }

}
