package model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
public class UserJPA {
   @Id
   @NotNull
   @Column(name = "username")
   private String username;

   @NotNull
   @Column(name = "password")
   private String password;

   @NotNull
   @Column(name = "enabled")
   private boolean enabled;

   @NotNull
   @Column(name = "first_name")
   private String firstName;

   @ElementCollection(fetch = FetchType.EAGER)
   @Valid
   @CollectionTable(
           name = "AUTHORITIES",
           joinColumns = @JoinColumn(
                   name = "username",
                   referencedColumnName = "username"
           )
   )
   private List<AuthoritiesJPA> userAuthorities = new ArrayList<>();

   public UserJPA() {}

   public UserJPA(String username, String password, boolean enabled, String firstName, List<AuthoritiesJPA> userAuthorities) {
      this.username = username;
      this.password = password;
      this.enabled = enabled;
      this.firstName = firstName;
      this.userAuthorities = userAuthorities;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public UserJPA buildPassword(String s) {
      password = s;
      return this;
   }

   public boolean isEnabled() {
      return enabled;
   }

   public String getFirstName() {
      return firstName;
   }

   public List<AuthoritiesJPA> getUserAuthorities() {
      return userAuthorities;
   }

   public String[] userAuthoritiesStringArr(){
      List<AuthoritiesJPA> authorities = getUserAuthorities();
      String[] result = new String[authorities.size()];
      for (int i = 0; i < authorities.size(); i++) {
         AuthoritiesJPA authoritiesJPA = authorities.get(i);
         result[i] = authoritiesJPA.getAuthority();
      }
      return result;
   }

   @Override
   public String toString() {
      return "User{" +
              "username='" + username + '\'' +
              ", password=" + password +
              ", enabled=" + enabled +
              ", firstName=" + firstName +
              ", orderRows=" + userAuthorities.toString() +
              '}';
   }
}

