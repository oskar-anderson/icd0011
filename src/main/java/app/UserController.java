package app;

import conf.DbConfig;
import dao.UserDaoJPA;
import model.UserJPA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
public class UserController {

    private final UserDaoJPA dao;

    public UserController(){
        var ctx = new AnnotationConfigApplicationContext(DbConfig.class);
        this.dao = ctx.getBean(UserDaoJPA.class);
    }

    @GetMapping("/")
    public String frontPage() {
        return "Front page!";
    }

    @GetMapping("/api/version")
    public String version() {
        return "Version 1";
    }

    @GetMapping("/count")
    public String counter(HttpSession session) {

        Object count = session.getAttribute("count");

        count = count instanceof Integer
                ? (Integer) count + 1
                : 0;

        session.setAttribute("count", count);

        return String.valueOf(count);
    }

    @GetMapping("/api/home")
    public String home() {
        return "Api home url";
    }

    @GetMapping("/api/info")
    public String info(Principal principal) {
        String user = principal == null ? "" : principal.getName();

        return "Current user: " + user;
    }

    @GetMapping("/api/admin/info")
    public String adminInfo(Principal principal) {
        return "Admin user info: " + principal.getName();
    }

    @GetMapping("/api/users")
    public String getUserByName(Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // return "Admin privileges granted" // is the same result as
            // throw new ResponseStatusException(HttpStatus.OK, "Admin privileges granted");
            throw new ResponseStatusException(HttpStatus.OK, "Admin privileges granted");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }
    }

    @GetMapping("/api/users/{userName}")
    public UserJPA getUserByName(@PathVariable String userName, Authentication auth) {
        UserJPA dbUser = null;
        try {
            dbUser = dao.findUserByUsername(userName);
        } catch (javax.persistence.NoResultException ignored){

        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            if (dbUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND");
            }
            return dbUser;
        }
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            if (!auth.getPrincipal().equals(userName)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
            }
            if (dbUser == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND");
            }
            return dbUser;

        }
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "NOT_IMPLEMENTED");
    }

}