package main;

import global.Global;
import org.hsqldb.Server;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;


public class Main {

   public static void main(String[] args) {
      Server server = new Server();
      server.setDatabasePath(0, "mem:db1;sql.syntax_pgs=true");
      server.setDatabaseName(0, "db1");
      server.setLogWriter(new Logger(System.out));
      server.start();
   }

   private static class Logger extends PrintWriter {
      public Logger(OutputStream out) {
         super(out);
      }

      @Override
      public void print(String line) {
         if (isSql(line)) {
            Global.print(line);
         }
      }

      @Override
      public void println() {
      }

      private boolean isSql(String line) {
         var keywords = List.of("insert", "create", "select", "alter");
         for (String key : keywords) {
            if (line.toLowerCase(Locale.ENGLISH).contains(key)) {
               return true;
            }
         }
         return false;
      }
   }

}
