package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.Global;
import model.Order;
import util.ContextAttribute;
import util.JsonHandling;
import util.IntOrString;
import util.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        List<Order> orders = new ContextAttribute().getOrders(getServletContext());
        Global.printLine(id);


        if (id == null) {
            resp.setContentType(Global.TEXT_PLAIN);
            resp.getWriter().println("All orders");
            for (Order order : orders) {
                String response = new ObjectMapper().writeValueAsString(order);
                resp.getWriter().println(response);
            }
        } else {
            resp.setContentType(Global.APPLICATION_JSON);
            for (Order order : orders) {
                if (order.getId().equals(id)) {
                    String response = new ObjectMapper().writeValueAsString(order);
                    resp.getWriter().println(response);
                }
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        String json = Util.readStream(req.getInputStream());
        Order order = new ObjectMapper().readValue(json, Order.class);
        order.setId(UUID.randomUUID().toString());

        new ContextAttribute().storeOrder(order, getServletContext());
        Global.printLine("added order with id " + order.getId());
        resp.setContentType(Global.APPLICATION_JSON);
        String response = new ObjectMapper().writeValueAsString(order);
        resp.getWriter().println(response);

    }
}
