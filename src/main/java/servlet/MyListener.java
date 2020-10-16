package servlet;

import config.Config;
import config.HsqlDataSource;
import config.PostgresDataSource;
import global.Global;
import jdbc.Main;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.ConnectionPoolFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
public class MyListener implements ServletContextListener {


   @Override
   public void contextInitialized(ServletContextEvent servletContextEvent) {
      Global.printLine("MyListener started...");

      BasicDataSource pool = (BasicDataSource) new ConnectionPoolFactory().createConnectionPool();
      Global.printLine("Pool created");
      Main.getTemplate(pool);
      try {
         pool.close();
      } catch (SQLException e) {
         throw new RuntimeException("Pool was not closed", e);
      }
      Global.printLine("DB Table created");

      ServletContext context = servletContextEvent.getServletContext();
      ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
              Config.class,
              PostgresDataSource.class,
              HsqlDataSource.class);
      Global.environment = ctx.getEnvironment();
      context.setAttribute(Global.CTX, ctx);
      Global.printLine("ConfigurableApplicationContext created");
   }

   @Override
   public void contextDestroyed(ServletContextEvent servletContextEvent) {
   }

   public static void insertDummyData() {
      BasicDataSource pool = (BasicDataSource) new ConnectionPoolFactory().createConnectionPool();
      Main.getTemplate(pool);
      try {
         pool.close();
      } catch (SQLException e) {
         throw new RuntimeException("Pool was not closed", e);
      }
   }
}
