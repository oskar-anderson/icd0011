package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.Global;
import jdbc.OrderDao;
import model.Order;
import org.apache.commons.dbcp2.BasicDataSource;
import util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        Global.printLine(id);

        DataSource pool2 = (DataSource) getServletContext().getAttribute(Global.POOL);
        OrderDao orderDao2 = new OrderDao(pool2);
        List<Order> orders = orderDao2.findOrders();


        resp.setContentType(Global.APPLICATION_JSON);
        if (id == null) {
            List<String> response = new ArrayList<>();
            for (Order order : orders) {
                response.add(new ObjectMapper().writeValueAsString(order));
            }
            resp.getWriter().println(response);
        } else {
            Order correctOrder = orders.stream()
                    .filter(order -> order.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            if (correctOrder != null) {
                String response = new ObjectMapper().writeValueAsString(correctOrder);
                resp.getWriter().println(response);
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        String json = Util.readStream(req.getInputStream());
        Order order = new ObjectMapper().readValue(json, Order.class);

        DataSource pool = (DataSource) getServletContext().getAttribute(Global.POOL);
        OrderDao orderDao = new OrderDao(pool);
        order = orderDao.insertOrder(order);

        Global.printLine("added order with id " + order.getId());
        resp.setContentType(Global.APPLICATION_JSON);
        String response = new ObjectMapper().writeValueAsString(order);
        resp.getWriter().println(response);

    }
}
