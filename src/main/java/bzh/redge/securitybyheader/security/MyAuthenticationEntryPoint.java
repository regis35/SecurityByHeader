package bzh.redge.securitybyheader.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            Map<String, Object> responseMsg = new HashMap<>(1);
            responseMsg.put("message", authException.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            try {
                response.setContentType("application/json;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter writer = response.getWriter();
                writer.write(mapper.writeValueAsString(responseMsg));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                log.error("Error when write response");
                // ignore
            }
        }
    }
}
