package jdbc;

import config.Config;
// import config.PostgresDataSource;
import model.Order;
import model.OrderRow;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import servlet.MyListener;

import javax.sql.DataSource;

public class Main {

   public static void main(String[] args) throws Exception {
/*
      MyListener.insertDummyData();
      OrderRow orderRow1 = new OrderRow();
      orderRow1.setItemName("cheese");
      orderRow1.setQuantity(7);
      orderRow1.setPrice(4);
      OrderRow orderRow2 = new OrderRow();
      orderRow2.setItemName("ham");
      orderRow2.setQuantity(3);
      orderRow2.setPrice(9);
      Order order = new Order();
      order.setOrderNumber("test_abc");
      order.add(orderRow1);
      order.add(orderRow2);
      var ctx = new AnnotationConfigApplicationContext(Config.class, PostgresDataSource.class);
      try (ctx) {
         OrderDao orderDao = ctx.getBean(OrderDao.class);
         order = orderDao.insertOrder(order);
         System.out.println(order.toString());
      }
      catch (Exception e) { throw new RuntimeException(e); }
      System.out.println("finished");
      */

   }

   @Bean
   public static JdbcTemplate getTemplate(DataSource dataSource) {
      var populator = new ResourceDatabasePopulator(
              new ClassPathResource("schema.sql"),
              new ClassPathResource("data.sql"));

      DatabasePopulatorUtils.execute(populator, dataSource);

      return new JdbcTemplate(dataSource);
   }
}
