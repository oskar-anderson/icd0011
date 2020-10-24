package app;

import model.Order;
import model.OrderRow;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Installment {
   private Integer amount;

   private LocalDate date;

   public Integer getAmount() {
      return amount;
   }

   public void setAmount(Integer amount) {
      this.amount = amount;
   }

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }

   public Installment(int amount, LocalDate date) {
      this.date = date;
      this.amount = amount;
   }

   public static List<Installment> dividePayment(Order order, String sStartDate, String sEndDate) {
      List<OrderRow> orderRows = order.getOrderRows();
      int sum = 0;
      for (OrderRow orderRow : orderRows) {
         sum += orderRow.getPrice() * orderRow.getQuantity();
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate ldStart = LocalDate.parse(sStartDate, formatter);
      LocalDate ldEnd = LocalDate.parse(sEndDate, formatter);

      int paymentCount = (ldEnd.getYear() - ldStart.getYear()) * 12 + ldEnd.getMonthValue() - ldStart.getMonthValue() + 1;
      if (paymentCount == 0) { return new ArrayList<>(); }

      int startingPayment = sum / paymentCount;
      if (startingPayment < 3) {
         startingPayment = 3;
      }
      paymentCount = sum / startingPayment;

      int paymentToBeAddedLater = sum % paymentCount;
      List<Installment> payments = new ArrayList<>();
      LocalDate paymentDate = ldStart;

      for (int i = 0; i < paymentCount; i++) {
         payments.add(new Installment(startingPayment, paymentDate));
         paymentDate = paymentDate.plusMonths(1);
         paymentDate = paymentDate.plusDays(-paymentDate.getDayOfMonth() + 1);
      }
      int lastElement = payments.size() - 1;
      for (int i = lastElement; i > lastElement - paymentToBeAddedLater; i--) {
         payments.get(i).amount++;
      }
      return payments;
   }

   @Override
   public String toString() {
      return MessageFormat.format("amount: {0}; date: {1}",
              amount, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
   }
}
