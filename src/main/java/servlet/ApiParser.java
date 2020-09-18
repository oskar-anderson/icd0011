package servlet;

import util.JsonHandling;
import util.IntOrString;
import util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/parser")
public class ApiParser extends HttpServlet {
   @Override
   protected void doGet(HttpServletRequest request,
                        HttpServletResponse response)
           throws IOException {

      response.getWriter().print("Hello from api parser!");
   }

   @Override
   protected void doPost(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
      String json = Util.readStream(req.getInputStream());

      Map<String, IntOrString> map = JsonHandling.jsonToMap(json);
      for (Map.Entry<String, IntOrString> stringObjectEntry : map.entrySet()) {
         String key = stringObjectEntry.getKey();
         IntOrString value = stringObjectEntry.getValue();
         if (key.length() <= 3) {
            continue;
         }
         switch (value.type) {
            case INTEGER_:
               value.setO(Integer.parseInt(String.valueOf(value.getO())) * 2);
               break;
            case STRING_:
               value.setO(key);
               break;
            default:
               throw new RuntimeException("Should not happen!");

         }
      }
      json = JsonHandling.mapToString(map);

      resp.setContentType("application/json");
      resp.getWriter().println(json);
      //Post post = new ObjectMapper().readValue(json, Post.class);
   }
}
