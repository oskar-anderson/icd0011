package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {
   @NonNull
   private String code;
   private List<String> arguments;

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public List<String> getArguments() {
      return arguments;
   }

   public void setArguments(List<String> arguments) {
      this.arguments = arguments;
   }

   @Override
   public String toString() {
      return "ValidationError{" +
              "code='" + code + '\'' +
              ", arguments=" + arguments +
              '}';
   }


   public ValidationError buildCode(String code) {
      this.code = code;
      return this;
   }

   public ValidationError buildArguments(List<String> arguments) {
      this.arguments = arguments;
      return this;
   }
}
