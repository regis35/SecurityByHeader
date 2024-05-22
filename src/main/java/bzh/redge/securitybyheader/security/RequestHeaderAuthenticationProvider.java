package bzh.redge.securitybyheader.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RequestHeaderAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Start authenticate");
        if(!"REGIS".equals(authentication.getPrincipal())) {
            log.error("Bad authentication");
            throw new BadCredentialsException("Bad Request Header Credentials");
        }
        authentication.setAuthenticated(true);
        log.info("Authenticated");
        return new CustomAuthentication(authentication.getPrincipal());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
