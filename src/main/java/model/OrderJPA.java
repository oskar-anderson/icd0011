package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.validator.constraints.Length;

//import org.hibernate.annotations.Entity; //this is wrong
import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderJPA {
   @Id
   @SequenceGenerator(name = "my_seq", sequenceName = "seq3", allocationSize = 1)
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq")
   private Long id;

   @Length(min = 2)
   @Column(name = "order_number")
   private String orderNumber;

   @ElementCollection(fetch = FetchType.EAGER)
   @Valid
   @CollectionTable(
           name = "order_rows",
           joinColumns = @JoinColumn(
                   name = "orders_id",
                   referencedColumnName = "id"
           )
   )
   private List<OrderRowJPA> orderRows = new ArrayList<>();

   public OrderJPA() {}

   public OrderJPA(String orderNumber) {
      this.orderNumber = orderNumber;
   }

   public OrderJPA(Long id, String orderNumber) {
      this.id = id;
      this.orderNumber = orderNumber;
   }
   public OrderJPA(Long id, String orderNumber, List<OrderRowJPA> orderRows) {
      this.id = id;
      this.orderNumber = orderNumber;
      this.orderRows = orderRows;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getOrderNumber() {
      return orderNumber;
   }

   public void setOrderNumber(String orderNumber) {
      this.orderNumber = orderNumber;
   }

   public List<OrderRowJPA> getOrderRows() {
      return orderRows;
   }

   public void add(OrderRowJPA orderRow) {
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
