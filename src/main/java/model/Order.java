package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import java.util.ArrayList;
import java.util.List;

public class Order {
   private String id;
   private String orderNumber;
   private List<OrderRow> orderRows;

   public Order() {}

   public Order(String orderNumber) {
      this.orderNumber = orderNumber;
   }

   public Order(String id, String orderNumber) {
      this.id = id;
      this.orderNumber = orderNumber;
   }
   public Order(String id, String orderNumber, List<OrderRow> orderRows) {
      this.id = id;
      this.orderNumber = orderNumber;
      this.orderRows = orderRows;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getOrderNumber() {
      return orderNumber;
   }

   public void setOrderNumber(String orderNumber) {
      this.orderNumber = orderNumber;
   }

   public List<OrderRow> getOrderRows() {
      return orderRows;
   }

   public void add(OrderRow orderRow) {
      if (orderRows == null) {
         orderRows = new ArrayList<>();
      }

      orderRows.add(orderRow);
   }

   @Override
   public String toString() {
      return "Order{" +
              "id=" + id +
              ", orderNumber='" + orderNumber + '\'' +
              ", orderRows=" + orderRows +
              '}';
   }

   @Override
   public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
   }

   @Override
   public int hashCode() {
      return 1;
   }
}
