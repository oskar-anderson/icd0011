package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.Global;
import jdbc.OrderDao;
import model.Order;
import model.ValidationError;
import model.ValidationErrors;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        Global.printLine("Get with id: " + id);
        // 9
        // var ctx = new AnnotationConfigApplicationContext(Config.class, PostgresDataSource.class);
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) getServletContext().getAttribute(Global.CTX);

        OrderDao orderDao = ctx.getBean(OrderDao.class);

        resp.setContentType(Global.APPLICATION_JSON);
        if (id == null) {
            List<Order> orders = orderDao.findOrders();
            List<String> response = new ArrayList<>();
            for (Order order : orders) {
                response.add(new ObjectMapper().writeValueAsString(order));
            }
            resp.getWriter().println(response);
        } else {
            if (id.matches("-?\\d+")) {
                long idLong = Long.parseLong(id);
                Order order = orderDao.findOrderById(idLong);
                String response = new ObjectMapper().writeValueAsString(order);
                resp.getWriter().println(response);
                return;
            }
            resp.setStatus(404);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        String json = Util.readStream(req.getInputStream());
        Order order = new ObjectMapper().readValue(json, Order.class);

        resp.setContentType(Global.APPLICATION_JSON);
        var errors = new ValidationErrors();
        List<ValidationError> errorList = new ArrayList<>();

        if (order.getOrderNumber().length() < 2) {
            ValidationError error1 = new ValidationError()
                    .buildArguments(Collections.singletonList("too_short_number"))
                    .buildCode("WAT");
            errorList.add(error1);
            errors.setErrors(errorList);
            resp.setStatus(400);
            String response = new ObjectMapper().writeValueAsString(errors);
            resp.getWriter().println(response);
            return;
        }
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) getServletContext().getAttribute(Global.CTX);
        //var ctx = new AnnotationConfigApplicationContext(Config.class, PostgresDataSource.class);

        OrderDao orderDao = ctx.getBean(OrderDao.class);
        long startTime = System.nanoTime();
        order = orderDao.insertOrder(order);
        long elapsedTime = System.nanoTime() - startTime;
        long duration = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);

        Global.printLine("insertion time (MILLISECONDS):" + duration);
        Global.printLine("Post successful. Added order with id: " + order.getId());
        String response = new ObjectMapper().writeValueAsString(order);
        resp.getWriter().println(response);


    }

    @Override
    protected void doDelete(HttpServletRequest req,
                          HttpServletResponse resp) {
        String idString = req.getParameter("id");

        if (idString != null) {
            if (idString.matches("-?\\d+")){
                long idLong = Long.parseLong(idString);
                ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) getServletContext().getAttribute(Global.CTX);
                //var ctx = new AnnotationConfigApplicationContext(Config.class, PostgresDataSource.class);

                OrderDao orderDao = ctx.getBean(OrderDao.class);
                boolean succ = orderDao.deleteOrder(idLong);
                if (succ) {
                    resp.setStatus(200);
                    Global.printLine("Deleted order with id: " + idLong);
                    return;
                }
                resp.setStatus(404);
                Global.printLine("Deletion failed, no such id: " + idLong);
                return;

            }
            Global.printLine("Deletion failed. Integer required. Was: " + idString);
        }
        resp.setStatus(400);
    }
}
