package jdbc;

import global.Global;
import model.Order;
import model.OrderRow;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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
      return Global.unnecessaryLocalBeforeReturn(queryResult);
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
      return queryResult.get(0);
   }

   public Order insertOrder(Order order) {
      var dataOrder = new BeanPropertySqlParameterSource(order);
      // var data = Map.of("OrderNumber", order.getOrderNumber());
      Number idOrder = new SimpleJdbcInsert(template)
              .withTableName("order_")
              .usingGeneratedKeyColumns("id")
              .executeAndReturnKey(dataOrder);
      order.setId(idOrder.longValue());


      List<OrderRow> orderRows = order.getOrderRows() == null ? new ArrayList<>() : order.getOrderRows();
      for (OrderRow orderRow : orderRows) {
         orderRow.setOrderId(idOrder.longValue());
         Long idOrderRow = insertOrderRow(orderRow);
         orderRow.setId(idOrderRow);
      }
      return order;

   }

   private Long insertOrderRow(OrderRow orderRow){
      var data = new BeanPropertySqlParameterSource(orderRow);
      Number idOrderRow = new SimpleJdbcInsert(template)
              .withTableName("orderRow")
              .usingGeneratedKeyColumns("id")
              .executeAndReturnKey(data);
      return idOrderRow.longValue();
   }

   public boolean deleteOrder(Long id) {
      String sql = "delete from order_ where id = ?;";
      Object[] args = { id };
      var result = template.update(sql, args);
      return result == 1;
   }


   private static class OrderHandler implements RowCallbackHandler {
      private final Map<Long, Order> ordersMap = new HashMap<>();
      private final List<Order> result = new ArrayList<>();

      @Override
      public void processRow(ResultSet rs) throws SQLException {
         Long orderId = Long.parseLong(rs.getString("orderId"));
         if (ordersMap.get(orderId) == null) {
            Order order = new Order(orderId, rs.getString("orderNumber"));
            ordersMap.put(orderId, order);
            result.add(order);
         }
         String orderRowId = rs.getString("orderRowId");
         if (orderRowId == null) {
            return;
         }
         Long id = Long.parseLong(orderRowId);
         String itemName = rs.getString("itemName");
         Integer quantity = rs.getInt("quantity");
         Integer price = rs.getInt("price");

         ordersMap.get(orderId).add(new OrderRow(id, orderId, itemName, quantity, price));
      }
   }
}
