package jdbc;

import model.Order;
import model.OrderRow;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class OrderDao {

   private final DataSource dataSource;

   public OrderDao(DataSource dataSource) {
      this.dataSource = dataSource;
   }


   public List<Order> findOrders() {
      try (
              Connection conn = dataSource.getConnection();
              Statement stmt = conn.createStatement()
      ) {

         ResultSet rs = stmt.executeQuery(
                "select " +
                "order_.id as orderId, " +
                "order_.orderNumber, " +
                "orderRow.id as orderRowId, " +
                "orderRow.itemName, " +
                "orderRow.quantity, " +
                "orderRow.price " +
                "from order_ " +
                "left join orderRow ON order_.id = orderRow.orderId");

         Map<String, Order> ordersMap = new HashMap<>();
         List<Order> orders = new ArrayList<>();
         while (rs.next()){
            String orderId = rs.getString("orderId");
            Order order = ordersMap.get(orderId);
            if (order == null) {
               order = new Order(orderId, rs.getString("orderNumber"));
               ordersMap.put(orderId, order);
               orders.add(order);
            }
            addOrderRowsToOrder(rs, order);
         }

         return orders;


      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }


   public Order findOrderById(long id) {
      String sql = "select " +
                   "order_.id as orderId, " +
                   "order_.orderNumber, " +
                   "orderRow.id as orderRowId, " +
                   "orderRow.itemName, " +
                   "orderRow.quantity, " +
                   "orderRow.price " +
                   "from order_ " +
                   "left join orderRow ON order_.id = orderRow.orderId " +
                   "where order_.id = ?";

      try (
              Connection conn = dataSource.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)
      ) {
         ps.setLong(1, id);

         ResultSet rs = ps.executeQuery();

         Order order = null;
         while (rs.next()){
            if (order == null) {
               String orderId = rs.getString("orderId");
               order = new Order(orderId, rs.getString("orderNumber"));
            }
            addOrderRowsToOrder(rs, order);
         }

         return order;


      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }

   private void addOrderRowsToOrder(ResultSet rs, Order order) throws SQLException {
      String orderRowId = rs.getString("orderRowId");
      if (orderRowId == null) {
         return;
      }
      String itemName = rs.getString("itemName");
      Integer quantity = rs.getInt("quantity");
      Integer price = rs.getInt("price");

      OrderRow orderRow = new OrderRow(order.getId(), itemName, quantity, price);
      order.add(orderRow);
   }


   public Order insertOrder(Order order) {

      try (Connection conn = dataSource.getConnection()) {
         conn.setAutoCommit(false);
         String sql = "insert into order_ (orderNumber) values (?)";
         Order result;

         try (PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"}))
         {

            ps.setString(1, order.getOrderNumber());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) { throw new RuntimeException("unexpected"); }

            result = new Order(
                    rs.getString("id"),
                    order.getOrderNumber());

         } catch (SQLException e) { throw new RuntimeException(e); }

         sql = "insert into orderRow (orderId, itemName, quantity, price) values (?,?,?,?)";

         List<OrderRow> orderRows = order.getOrderRows() == null ? new ArrayList<>() : order.getOrderRows();
         for (OrderRow orderRow : orderRows) {
            try (PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"}))
            {

               ps.setBigDecimal(1, new BigDecimal(result.getId()));
               ps.setString(2, orderRow.getItemName());
               ps.setInt(3, orderRow.getQuantity());
               ps.setInt(4, orderRow.getPrice());
               ps.executeUpdate();

               ResultSet rs = ps.getGeneratedKeys();
               if (!rs.next()) { throw new RuntimeException("unexpected"); }


               result.add(new OrderRow(
                       rs.getString("id"),
                       orderRow.getItemName(),
                       orderRow.getQuantity(),
                       orderRow.getPrice()
               ));

            } catch (SQLException e) { throw new RuntimeException(e); }
         }

         conn.commit();
         return result;
      } catch (SQLException e) { throw new RuntimeException(e); }
   }

   public boolean deleteOrder(Long id) {
      String sql = "delete from order_ where id=?";
      try (Connection conn = dataSource.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"})) {

         ps.setLong(1, id);
         int affectedRows = ps.executeUpdate();

         return affectedRows != 0;
      } catch (SQLException e) { throw new RuntimeException(e); }
   }

}
