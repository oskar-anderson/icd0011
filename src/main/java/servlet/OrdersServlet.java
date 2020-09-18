package servlet;

import model.Post;
import util.JsonHandling;
import util.IntOrString;
import util.Util;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/api/orders")
public class OrdersServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        ServletContext context = getServletContext();
        Post post = (Post) context.getAttribute("attrib1");
        response.getWriter().print("Hello!" + post.getTitle());
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException {
        String json = Util.readStream(req.getInputStream());

        Map<String, IntOrString> map = JsonHandling.jsonToMap(json);
        UUID uuid = UUID.randomUUID();
        map.put("id", new IntOrString(uuid.toString()));
        json = JsonHandling.mapToString(map);


        resp.setContentType("application/json");
        resp.getWriter().println(json);

        //Post post = new ObjectMapper().readValue(json, Post.class);

    }


}
