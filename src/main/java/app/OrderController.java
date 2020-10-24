package app;

import jdbc.OrderDao;
import model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static global.Global.*;

@RestController
public class OrderController {

   private final OrderDao dao;

   public OrderController(OrderDao dao){
      this.dao = dao;
   }

   @GetMapping("orders/{id}")
   protected Order doGet(@PathVariable Long id) {
      Order order = dao.findOrderById(id);
      print("Get order. id= " + id + ": " + order);
      return unnecessaryLocalBeforeReturn(order);
   }

   @GetMapping("orders")
   protected List<Order> doGet() {
      List<Order> orders = dao.findOrders();
      print("Get all orders:" + orders);
      return unnecessaryLocalBeforeReturn(orders);
   }

   @GetMapping("orders/{id}/installments")
   protected List<Installment> doGetPaymentLogic(@PathVariable Long id, @RequestParam(name = "start") String startDate, @RequestParam(name = "end") String endDate) {
      Order order = dao.findOrderById(id);
      List<Installment> payments = Installment.dividePayment(order, startDate, endDate);
      print("PaymentSchedule:" + payments);
      return unnecessaryLocalBeforeReturn(payments);
   }

   @PostMapping("orders")
   // @ResponseStatus(HttpStatus.CREATED)  // only 200 is good for us;
   protected Order doPost(@RequestBody @Valid Order order) {
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
