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
        Global.printLine("Get with id: " + id);

        DataSource pool = (DataSource) getServletContext().getAttribute(Global.POOL);
        OrderDao orderDao = new OrderDao(pool);


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

        DataSource pool = (DataSource) getServletContext().getAttribute(Global.POOL);
        OrderDao orderDao = new OrderDao(pool);
        order = orderDao.insertOrder(order);

        Global.printLine("Post successful. Added order with id: " + order.getId());
        resp.setContentType(Global.APPLICATION_JSON);
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
                DataSource pool = (DataSource) getServletContext().getAttribute(Global.POOL);
                OrderDao orderDao = new OrderDao(pool);
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
