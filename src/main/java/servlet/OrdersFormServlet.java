package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.Global;
import model.Order;
import util.ContextAttribute;
import util.Util;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/orders/form")
public class OrdersFormServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        List<Order> orders = new ContextAttribute().getOrders(getServletContext());
        Global.printLine(id);

        resp.setContentType(Global.TEXT_PLAIN);
        if (id == null) {
            resp.getWriter().println("All orders");
            for (Order order : orders) {
                String response = new ObjectMapper().writeValueAsString(order);
                resp.getWriter().println(response);
            }
        } else {
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
        String s = Util.readStream(req.getInputStream());
        s = s.trim().replaceAll("orderNumber=", "");

        UUID uuid = UUID.randomUUID();
        Order order = new Order(uuid.toString(), s);

        new ContextAttribute().storeOrder(order, getServletContext());
        Global.printLine("added order with id " + order.getId());

        resp.setContentType(Global.TEXT_PLAIN);
        resp.getWriter().print(order.getId());


    }
}
