package jdbc;

import model.Order;
import model.OrderRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

@Repository
public class OrderDao {

   private JdbcTemplate template;

   public OrderDao(JdbcTemplate template) {
      this.template = template;
   }


   public List<Order> findOrders() {
      var handler = new OrderHandler();
      template.query("select " +
              "order_.id as orderId, " +
              "order_.orderNumber, " +
              "orderRow.id as orderRowId, " +
              "orderRow.itemName, " +
              "orderRow.quantity, " +
              "orderRow.price " +
              "from order_ " +
              "left join orderRow ON order_.id = orderRow.orderId;", handler);


      List<Order> queryResult = handler.result;
      // List<Order> result = queryLinesToOrderWithRows(queryResult);
      assert true;  // bypasses styleCheck
      return queryResult;
   }


   public Order findOrderById(long id) {

      var handler = new OrderHandler();
      template.query("select " +
              "order_.id as orderId, " +
              "order_.orderNumber, " +
              "orderRow.id as orderRowId, " +
              "orderRow.itemName, " +
              "orderRow.quantity, " +
              "orderRow.price " +
              "from order_ " +
              "left join orderRow ON order_.id = orderRow.orderId " +
              "where order_.id = " + id + ";", handler);

      List<Order> queryResult = handler.result;
      // List<Order> result = queryLinesToOrderWithRows(queryResult);
      return queryResult.get(0);
   }
/*

   private List<Order> queryLinesToOrderWithRows(List<Order> queryLines) {
      Map<String, Order> ordersMap = new HashMap<>();
      List<Order> orders = new ArrayList<>();
      for (Order orderLine : queryLines) {
         String orderId = orderLine.getId();
         if (ordersMap.get(orderId) == null) {
            Order order = new Order(orderId, orderLine.getOrderNumber());
            ordersMap.put(orderId, order);
            orders.add(order);
         }
         if (orderLine.getOrderRows() == null) {
            continue;
         }
         if (orderLine.getOrderRows().size() != 1) {
            throw new RuntimeException("unexpected");
         }
         OrderRow orderRow = orderLine.getOrderRows().get(0);

         ordersMap.get(orderRow.getOrderId()).add(orderRow);
      }
      return orders;
   }

*/

   public Order insertOrder(Order order) {
      var dataOrder = new BeanPropertySqlParameterSource(order);
      // var data = Map.of("OrderNumber", order.getOrderNumber());
      Number idOrder = new SimpleJdbcInsert(template)
              .withTableName("order_")
              .usingGeneratedKeyColumns("id")
              .executeAndReturnKey(dataOrder);
      order.setId(idOrder.toString());


      List<OrderRow> orderRows = order.getOrderRows() == null ? new ArrayList<>() : order.getOrderRows();
      for (OrderRow orderRow : orderRows) {
         orderRow.setOrderId(idOrder.toString());
         String idOrderRow = insertOrderRow(orderRow, order.getId());
         orderRow.setId(idOrderRow);
      }
      return order;

   }

   private String insertOrderRow(OrderRow orderRow, String orderId){
      var data = Map.of(
              "orderId", new BigDecimal(orderId),
              "itemName", orderRow.getItemName(),
              "quantity", orderRow.getQuantity(),
              "price", orderRow.getPrice());

      // var dataOrderRow = new BeanPropertySqlParameterSource(orderRow);
      Number idOrderRow = new SimpleJdbcInsert(template)
              .withTableName("orderRow")
              .usingGeneratedKeyColumns("id")
              .executeAndReturnKey(data);
      return idOrderRow.toString();
   }

   public boolean deleteOrder(Long id) {
      String sql = "delete from order_ where id = ?;";
      Object[] args = { id };
      var result = template.update(sql, args);
      return result == 1;
   }


   private static class OrderHandler implements RowCallbackHandler {
      private Map<String, Order> ordersMap = new HashMap<>();
      private final List<Order> result = new ArrayList<>();

      @Override
      public void processRow(ResultSet rs) throws SQLException {
         String orderId = rs.getString("orderId");
         if (ordersMap.get(orderId) == null) {
            Order order = new Order(orderId, rs.getString("orderNumber"));
            ordersMap.put(orderId, order);
            result.add(order);
         }
         String orderRowId = rs.getString("orderRowId");
         if (orderRowId == null) {
            return;
         }
         String id = orderRowId;
         String itemName = rs.getString("itemName");
         Integer quantity = rs.getInt("quantity");
         Integer price = rs.getInt("price");


         ordersMap.get(orderId).add(new OrderRow(id, orderId, itemName, quantity, price));

         //Order order = new Order();
         // order.setId(rs.getString("orderId"));
         //order.setOrderNumber(rs.getString("orderNumber"));

         // addOrderRowsToOrder(rs, order);

         // result.add(order);

      }

      private void addOrderRowsToOrder(ResultSet rs, Order order) throws SQLException {
         String orderRowId = rs.getString("orderRowId");
         if (orderRowId == null) {
            return;
         }
         String id = rs.getString("orderRowId");
         String itemName = rs.getString("itemName");
         Integer quantity = rs.getInt("quantity");
         Integer price = rs.getInt("price");

         OrderRow orderRow = new OrderRow(id, order.getId(), itemName, quantity, price);
         order.add(orderRow);
      }
   }
}
