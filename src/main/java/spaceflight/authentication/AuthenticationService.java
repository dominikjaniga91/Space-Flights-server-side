package spaceflight.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class AuthenticationService {

    static final long EXPIRATION_TIME = 86_400_000;
    static final String SIGNING_KEY = "SecretKey";
    static final String PREFIX = "Bearer ";

    static public void addToken(HttpServletResponse response, String username){
        String JwtToken = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();

        response.addHeader("Authorization", PREFIX + " " + JwtToken);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }

}
