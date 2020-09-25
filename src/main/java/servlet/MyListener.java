package servlet;

import global.Global;
import model.Order;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class MyListener implements ServletContextListener {


   @Override
   public void contextInitialized(ServletContextEvent servletContextEvent) {
      Global.printLine("MyListener started...");
      ServletContext context = servletContextEvent.getServletContext();
      if (Global.DUMMY_ORDERS) {
         context.setAttribute(Global.ORDERS, insertDummyData());
      }
      else {
         context.setAttribute(Global.ORDERS, new ArrayList<Order>());
      }
   }

   @Override
   public void contextDestroyed(ServletContextEvent servletContextEvent) {

   }

   private List<Order> insertDummyData(){
      Global.printLine("inserting dummy orders...");
      List<Order> orders = new ArrayList<>();
      orders.add(new Order("abc", "123"));
      orders.add(new Order("bcd", "234"));
      return orders;
   }
}
