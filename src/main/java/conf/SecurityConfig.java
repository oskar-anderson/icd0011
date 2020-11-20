package conf;

import conf.security.handlers.ApiAccessDeniedHandler;
import conf.security.handlers.ApiEntryPoint;
import conf.security.handlers.ApiLogoutSuccessHandler;
import conf.security.jwt.JwtAuthenticationFilter;
import conf.security.jwt.JwtAuthorizationFilter;
import dao.UserDaoJPA;
import model.UserJPA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:/application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.signing.key}")
    private String jwtKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/api/home").permitAll();
        http.authorizeRequests().antMatchers("/api/login").permitAll();
        http.authorizeRequests().antMatchers("/api/version").permitAll();
        http.authorizeRequests().antMatchers("/api/admin/**").hasRole("ADMIN");
        http.authorizeRequests().antMatchers("/api/**").authenticated();

        // no login page redirect on no access for role
        http.exceptionHandling()
                .authenticationEntryPoint(new ApiEntryPoint());
        http.exceptionHandling()
                .accessDeniedHandler(new ApiAccessDeniedHandler());

        // logout does not redirect
        http.logout().logoutSuccessHandler(new ApiLogoutSuccessHandler());

        // special mapping?
        http.logout().logoutUrl("/api/logout");

        // login token
        var apiLoginFilter = new JwtAuthenticationFilter(
                authenticationManager(), "/api/login", jwtKey);

        http.addFilterAfter(apiLoginFilter, LogoutFilter.class);

        // token check
        var jwtAuthFilter = new JwtAuthorizationFilter(authenticationManager(), jwtKey);
        http.addFilterBefore(jwtAuthFilter, LogoutFilter.class);

        // no cookie. We use jwt token.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // source:
        // https://www.youtube.com/watch?v=LKvrFltAgCQ&ab_channel=JavaBrains&fbclid=IwAR2-DeMmlC24z1l8eSDLt86p3t6AMX-_iIINEwoxNtNbgBUuiynTZpfSiMg
        // https://grobmeier.solutions/spring-security-5-using-jdbc.html
        builder
                .jdbcAuthentication()
                .dataSource(DbConfig.getDataSource())
                .passwordEncoder(getPasswordEncoder())
                .usersByUsernameQuery("select username,password,enabled "
                        + "from users "
                        + "where username = ?")
                .authoritiesByUsernameQuery("select username, authority "
                        + "from authorities "
                        + "where username = ?");

        /*
        // this is stupid, but it works
        var ctx = new AnnotationConfigApplicationContext(DbConfig.class);
        var dao = ctx.getBean(UserDaoJPA.class);
        List<UserJPA> users = dao.findUsers();
        UserDetailsManagerConfigurer
                <
                AuthenticationManagerBuilder,
                org
                        .springframework
                        .security
                        .config
                        .annotation
                        .authentication
                        .configurers
                        .provisioning
                        .InMemoryUserDetailsManagerConfigurer
                        <AuthenticationManagerBuilder>
                >
                .UserDetailsBuilder builderBuilder = null;
        for (int i = 0; i < users.size(); i++) {
            UserJPA user = users.get(i);
            if (i == 0){
                builderBuilder = builder
                        .inMemoryAuthentication()
                        .passwordEncoder(new BCryptPasswordEncoder())
                        .withUser(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.userAuthoritiesStringArr());
            } else {
                builderBuilder = builderBuilder
                        .and()
                        .withUser(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.userAuthoritiesStringArr());
            }

        }
        builder
                .jdbcAuthentication()
                .dataSource(restDataSource)
                .usersByUsernameQuery(getUserQuery())
                .authoritiesByUsernameQuery(getAuthoritiesQuery());

        builder.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("user")
                .password("$2a$10$/rIrWkGcU9p.OwynZ6vKXOaZZ5Two5vCU22M9yTMMYLRe/pmjxS8e")
                .roles("USER")
                .and()
                .withUser("admin")
                .password("$2a$10$1XONjmq2CNAutWQAK9HDduoJczhAhYulN1L3YHRMiVaiLlTSh9QB2")
                .roles("USER", "ADMIN");
        */
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}