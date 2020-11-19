package conf.security.jwt;

import conf.security.TokenInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private String jwtKey;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, String jwtKey) {
        super(authenticationManager);

        this.jwtKey = jwtKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenString = request.getHeader("Authorization");

        if (tokenString == null) {
            chain.doFilter(request, response);
            return;
        }

        TokenInfo tokenInfo = new JwtHelper(jwtKey).decode(tokenString);

        var authorities = tokenInfo.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        var springToken = new UsernamePasswordAuthenticationToken(
                tokenInfo.getUserName(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(springToken);

        chain.doFilter(request, response);
    }
}