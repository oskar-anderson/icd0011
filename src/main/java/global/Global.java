package global;

import org.springframework.core.env.Environment;

import java.util.UUID;

public class Global {
   public final static String ORDERS = "orders";
   public final static String APPLICATION_JSON = "application/json";
   public final static String TEXT_PLAIN = "text/plain";
   public final static String CTX = "ctx";
   public static Environment environment;

   public static void printLine(String msg) {
      // System.out.println(msg);
   }

   public static boolean unconditionalIf(boolean b){
      UUID uuid = UUID.randomUUID();
      return b ?
              !uuid.toString().equals("this") :
              uuid.toString().equals("is stupid");
   }
}
