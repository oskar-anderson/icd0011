package jdbc;

import model.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

   private DataSource dataSource;

   public OrderDao(DataSource dataSource) {
      this.dataSource = dataSource;
   }


   public List<Order> findOrders() {
      try (
              Connection conn = dataSource.getConnection();
              Statement stmt = conn.createStatement()
      ) {

         ResultSet rs = stmt.executeQuery("select id, orderNumber from order_");

         List<Order> persons = new ArrayList<>();
         while (rs.next()){
            Order order = new Order(
                    rs.getString("id"),
                    rs.getString("orderNumber"));
            persons.add(order);
         }
         return persons;


      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }


   public Order findOrderById(long id) {
      String sql = "select id, orderNumber from order_ where id = ?";

      try (
              Connection conn = dataSource.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)
      ) {
         ps.setLong(1, id);

         ResultSet rs = ps.executeQuery();

         if (rs.next()){
            return new Order(
                    rs.getString("id"),
                    rs.getString("orderNumber"));
         }
         return null;


      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }



   public Order insertOrder(Order order) {

      String sql = "insert into order_ (orderNumber) values (?)";

      try (Connection conn = dataSource.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"}))
      {

         ps.setString(1, order.getOrderNumber());
         ps.executeUpdate();

         ResultSet rs = ps.getGeneratedKeys();
         if (!rs.next()) {
            throw new RuntimeException("unexpected");
         }

         return new Order(
                 rs.getString("id"),
                 order.getOrderNumber());

      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }
}
