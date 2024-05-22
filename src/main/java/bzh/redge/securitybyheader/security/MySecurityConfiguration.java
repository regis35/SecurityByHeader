package bzh.redge.securitybyheader.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;

@EnableWebSecurity(debug = true)
@Configuration
@Slf4j
public class MySecurityConfiguration {

    @Autowired
    private RequestHeaderAuthenticationProvider provider;

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(policy -> policy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(requestHeaderAuthenticationFilter(),RequestHeaderAuthenticationFilter.class)
                .exceptionHandling(handl -> handl.authenticationEntryPoint(authEntryPoint))
                .authorizeHttpRequests(requests -> requests.requestMatchers(new AntPathRequestMatcher("/test/index/**")).authenticated()
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setPrincipalRequestHeader("x-ssl-my-cutsom-header");
        filter.setExceptionIfHeaderMissing(false);
        filter.setAuthenticationManager(authenticationManager());

        return filter;
    }

    @Bean
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(provider);
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ClientDetails clientDetails() {
        log.info("Search authentication information");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client client = new Client(auth.getName());
        log.info("Build client detail {}", client.dn());
        return new ClientDetails(client);
    }
}
