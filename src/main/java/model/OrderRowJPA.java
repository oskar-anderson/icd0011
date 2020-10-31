package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
@Table(name = "order_rows")
public class OrderRowJPA {

   @Column(name = "item_name")
   private String itemName;

   @NotNull
   @Column(name = "quantity")
   @Range(min = 1, max = Integer.MAX_VALUE)
   private Integer quantity;

   @NotNull
   @Column(name = "price")
   @Range(min = 1, max = Integer.MAX_VALUE)
   private Integer price;

   public OrderRowJPA() {}

   public OrderRowJPA(String itemName, Integer quantity, Integer price) {
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


   public OrderRowJPA buildItemName(String itemName) {
      this.itemName = itemName;
      return this;
   }

   public OrderRowJPA buildQuantity(Integer quantity) {
      this.quantity = quantity;
      return this;
   }

   public OrderRowJPA buildPrice(Integer price) {
      this.price = price;
      return this;
   }
}
