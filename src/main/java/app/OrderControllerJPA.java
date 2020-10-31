package app;

import config.DbConfig;
import dao.OrderDaoJPA;
import model.OrderJPA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static global.Global.print;
import static global.Global.unnecessaryLocalBeforeReturn;

@RestController
public class OrderControllerJPA {

   private final OrderDaoJPA dao;

   public OrderControllerJPA(){
      var ctx = new AnnotationConfigApplicationContext(DbConfig.class);
      this.dao = ctx.getBean(OrderDaoJPA.class);
   }

   @GetMapping("orders/{id}")
   protected OrderJPA doGet(@PathVariable Long id) {
      OrderJPA order = dao.findOrderById(id);
      print("Get order. id= " + id + ": " + order);
      return unnecessaryLocalBeforeReturn(order);
   }

   @GetMapping("orders")
   protected List<OrderJPA> doGet() {
      List<OrderJPA> orders = dao.findOrders();
      print("Get all orders:" + orders);
      return unnecessaryLocalBeforeReturn(orders);
   }

   @PostMapping("orders")
   // @ResponseStatus(HttpStatus.CREATED)  // only 200 is good for us;
   protected OrderJPA doPost(@RequestBody @Valid OrderJPA order) {
      order = dao.insertOrder(order);
      print("Post order. Added order " + order);
      return unnecessaryLocalBeforeReturn(order);
   }

   @DeleteMapping("orders/{id}")
   protected void doDelete(@PathVariable Long id) {
      boolean succ = dao.deleteOrder(id);
      if (! succ) {
         throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found, id= " + id);
      }
      print("Delete order. Id= " + id);
   }

}
