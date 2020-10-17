package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRow {
   private Long id;
   private Long orderId;
   private String itemName;
   private Integer quantity;
   private Integer price;

   public OrderRow() {}

   public OrderRow(Long id, Long orderId, String itemName, Integer quantity, Integer price) {
      this.id = id;
      this.orderId = orderId;
      this.itemName = itemName;
      this.quantity = quantity;
      this.price = price;
   }

   public Long getId() {
      return id;
   }

   public Long getOrderId() {
      return orderId;
   }

   public String getItemName() {
      return itemName;
   }

   public Integer getQuantity() {
      return quantity;
   }

   public Integer getPrice() {
      return price;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setOrderId(Long id) {
      this.orderId = id;
   }

   public void setItemName(String itemName) {
      this.itemName = itemName;
   }

   public void setQuantity(Integer quantity) {
      this.quantity = quantity;
   }

   public void setPrice(Integer price) {
      this.price = price;
   }


   @Override
   public String toString() {
      return "OrderRow{" +
              "id='" + id + "'" +
              "orderId='" + orderId + "'" +
              "itemName='" + itemName + '\'' +
              ", quantity=" + quantity +
              ", price=" + price +
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


   public OrderRow buildOrderRowId(Long id) {
      this.id = id;
      return this;
   }

   public OrderRow buildOrderId(Long id) {
      this.orderId = id;
      return this;
   }

   public OrderRow buildItemName(String itemName) {
      this.itemName = itemName;
      return this;
   }

   public OrderRow buildQuantity(Integer quantity) {
      this.quantity = quantity;
      return this;
   }

   public OrderRow buildPrice(Integer price) {
      this.price = price;
      return this;
   }
}
