package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
@Table(name = "AUTHORITIES")
public class AuthoritiesJPA {

   @Column(name = "authority")
   private String authority;

   public AuthoritiesJPA() {}

   public AuthoritiesJPA(String authority) {
      this.authority = authority;
   }

   public String getAuthority() {
      return authority;
   }

   @Override
   public String toString() {
      return "Authorities{" +
              "authority=" + authority +
              '}';
   }
}