package util;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonHandling {
   public static Map<String, IntOrString> jsonToMap(String json) {
      String[] pairs = json
              .replace("{", "")
              .replace("}", "")
              .split(",");
      Map<String, IntOrString> map = new LinkedHashMap<>();
      for (String pair : pairs) {
         String[] keyAndValue = pair.split(":");
         String key = keyAndValue[0]
                 .replace("\"", "")
                 .trim();
         if (key.equals("id")) {
            continue;
         }
         String stringValue = keyAndValue[1]
                 .replace("\"", "")
                 .trim();
         IntOrString realValue = new IntOrString(stringValue);

         map.put(key, realValue);
      }
      return map;
   }

   public static String mapToString(Map<String, IntOrString> map) {
      StringBuilder output = new StringBuilder();
      for (Map.Entry<String, IntOrString> stringObjectEntry : map.entrySet()) {
         if (! output.toString().equals("")){
            output.append(",");
         }
         output
                 .append("\"")
                 .append(stringObjectEntry.getKey())
                 .append("\"")
                 .append(":")
                 .append(stringObjectEntry.getValue().toStringWithPad());
      }
      output.insert(0, "{").append("}");
      return output.toString();
   }
}
