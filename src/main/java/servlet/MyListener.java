package servlet;

import global.Global;
import org.apache.commons.dbcp2.BasicDataSource;
import util.ConnectionPoolFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

import static jdbc.Main.createSchema;

@WebListener
public class MyListener implements ServletContextListener {


   @Override
   public void contextInitialized(ServletContextEvent servletContextEvent) {
      Global.printLine("MyListener started...");

      try {
         createSchema();
      } catch (SQLException e) {
         throw new RuntimeException("DB schema failed", e);
      }
      Global.printLine("DB Table created");

      ServletContext context = servletContextEvent.getServletContext();
      BasicDataSource pool = (BasicDataSource) new ConnectionPoolFactory().createConnectionPool();
      context.setAttribute(Global.POOL, pool);
      Global.printLine("Pool created");
   }

   @Override
   public void contextDestroyed(ServletContextEvent servletContextEvent) {
      ServletContext context = servletContextEvent.getServletContext();
      BasicDataSource pool = (BasicDataSource) context.getAttribute(Global.POOL);
      try {
         pool.close();
      } catch (SQLException e) {
         throw new RuntimeException("Pool was not closed", e);
      }
      Global.printLine("Connections closed");

   }
}
