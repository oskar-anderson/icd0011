package main;

import config.DbConfig;
import dao.OrderDaoJPA;
import model.OrderJPA;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static global.Global.print;

public class Tester {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx =
              new AnnotationConfigApplicationContext(
                      DbConfig.class);

        try (ctx) {

            OrderDaoJPA dao = ctx.getBean(OrderDaoJPA.class);

            OrderJPA order1 = new OrderJPA("JPA-order-1");
            OrderJPA order2 = new OrderJPA("JPA-order-2");
            OrderJPA order3 = new OrderJPA("JPA-order-3");
            dao.insertOrder(order1);
            dao.insertOrder(order2);
            dao.insertOrder(order3);

            var readEntity = dao.findOrders();
            for (OrderJPA orderJPA : readEntity) {
                print(orderJPA.toString());
            }
        }
    }
}