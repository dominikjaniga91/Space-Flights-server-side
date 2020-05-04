package spaceflight.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        var error = new CustomErrorMessage();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setDeveloperMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now());

        if(response.getHeader("expired") != null){
            error.setDetail("Please login again");
        }else{
            error.setDetail("Invalid username or password");
        }

        var mapper = new ObjectMapper();
        var errorMessage = mapper.writeValueAsString(error);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().print(errorMessage);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
