package jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import static config.MvcConfig.getDataSource;
import static global.Global.*;

public class Main {

   public static void main(String[] args) {
      OrderDao dao = new OrderDao(new JdbcTemplate(getDataSource()));
      print(dao.findOrders().toString());
   }
}
