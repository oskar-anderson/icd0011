package util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Util {

    public static String readStream(InputStream is) {
        try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

}
