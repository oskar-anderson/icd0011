package util;

public class IntOrString {
   public enum Type {
      STRING_,
      INTEGER_
   }

   private Object o;
   public Type type;

   public IntOrString(String value){
      this.type = tryParseInt(String.valueOf(value)) ? Type.INTEGER_ : Type.STRING_;
      switch (this.type) {
         case STRING_:
            this.o = value;
            break;
         case INTEGER_:
            this.o = Integer.parseInt(value);
            break;
         default:
            throw new RuntimeException("Should not happen!");
      }
   }

   public String toStringWithPad(){
      switch (this.type) {
         case STRING_:
            return "\"" + o + "\"";
         case INTEGER_:
            return o.toString();
         default:
            throw new RuntimeException("Cannot convert to string!");
      }
   }

   public void setO(Object o) {
      this.o = o;
   }

   public Object getO() {
      return o;
   }

   private boolean tryParseInt(String value) {
      try {
         Integer.parseInt(value);
         return true;
      } catch (NumberFormatException e) {
         return false;
      }
   }
}
