package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRow {
   private String itemName;
   private Integer quantity;
   private Integer price;

   public OrderRow() {}

   public OrderRow(String itemName, Integer quantity, Integer price) {
      this.itemName = itemName;
      this.quantity = quantity;
      this.price = price;
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

   @Override
   public String toString() {
      return "OrderRow{" +
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
}