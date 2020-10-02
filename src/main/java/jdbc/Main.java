package jdbc;

import model.Order;
import util.ConnectionInfo;
import util.ConnectionPoolFactory;
import util.DbUtil;
import util.FileUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

   public static void main(String[] args) throws Exception {
      createSchema();
      // DataSource pool = new ConnectionPoolFactory().createConnectionPool();
      // OrderDao orderDao = new OrderDao(pool);
      // Order order1 = orderDao.insertOrder(new Order(null, "h57h-7pqg"));
   }

   public static void createSchema() throws SQLException {
      ConnectionInfo connectionInfo = DbUtil.readConnectionInfo();

      Connection conn = DriverManager.getConnection(
              connectionInfo.getUrl(),
              connectionInfo.getUser(),
              connectionInfo.getPass());

      try (conn; Statement stmt = conn.createStatement()) {

         String sql1 = FileUtil.readFileFromClasspath("schema.sql");
         String sql2 = FileUtil.readFileFromClasspath("data.sql");

         stmt.executeUpdate(sql1);
         stmt.executeUpdate(sql2);


      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }
}
