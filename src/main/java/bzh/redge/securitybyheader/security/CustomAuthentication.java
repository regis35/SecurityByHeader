package bzh.redge.securitybyheader.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomAuthentication extends AbstractAuthenticationToken {

    private final Object principal;

    public CustomAuthentication(Object aPrincipal) {
        super(null);
        this.principal = aPrincipal;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
