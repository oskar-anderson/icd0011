package util;

import java.util.Properties;

public class DbUtil {

    public static ConnectionInfo readConnectionInfo() {
        Properties properties = PropertyLoader.loadApplicationProperties();

        return new ConnectionInfo(
                properties.getProperty("dbUrl"),
                properties.getProperty("dbUser"),
                properties.getProperty("dbPassword"));
    }

}
