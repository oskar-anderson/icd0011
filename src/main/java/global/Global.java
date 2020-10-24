package global;

import util.PropertyLoader;

import java.io.PrintStream;
import java.util.Properties;
import java.util.UUID;

public class Global {
   private static final Properties PROPERTIES = PropertyLoader.loadApplicationProperties();

   /**
    * Avoids unnecessaryLocalBeforeReturn stylecheck.
    * Makes debugging easier.
    * Alternative to assert true before return.
    */
   public static <T> T unnecessaryLocalBeforeReturn(T item){
      return item;
   }

   public static void print(String msg) {
      // METHOD 1
      // System.out.println(msg);  // NOPMD

      // METHOD 2
      // PrintStream out=System.out;
      // out.println(msg);
   }

   public static boolean unconditionalIf(boolean b){
      UUID uuid = UUID.randomUUID();
      return b ?
              !uuid.toString().equals("this") :
              uuid.toString().equals("is stupid");
   }

   public static Properties getProperties() {
      return PROPERTIES;
   }
}
