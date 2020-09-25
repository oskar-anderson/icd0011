package util;

import global.Global;
import model.Order;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

public class ContextAttribute extends HttpServlet {
   // getServletContext() gives no warnings, but throws exception if done inside this class
   // java.lang.IllegalStateException: ServletConfig has not been initialized

   public List<Order> getOrders(ServletContext servletContext) {
      List<Order> orders = (ArrayList<Order>) servletContext.getAttribute(Global.ORDERS);
      if (orders == null) {
         throw new RuntimeException("Could not get stored orders!");
      }
      return orders;
   }

   public boolean storeOrder(Order order, ServletContext servletContext) {
      List<Order> orders = getOrders(servletContext);
      orders.add(order);
      servletContext.setAttribute(Global.ORDERS, orders);
      return true;
   }
}
