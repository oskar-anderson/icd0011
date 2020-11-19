package conf.security.jwt;

import conf.security.ApiAuthenticationFilter;
import conf.security.TokenInfo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends ApiAuthenticationFilter {

    private String jwtKey;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   String url, String jwtKey) {

        super(authenticationManager, url);

        this.jwtKey = jwtKey;
    }


    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain, Authentication authResult) {

        User user = (User) authResult.getPrincipal();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        String token = new JwtHelper(jwtKey)
                .encode(new TokenInfo(user.getUsername(), roles));

        response.addHeader("Authorization", "Bearer " + token);
    }
}
