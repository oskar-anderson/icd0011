package util;

import global.Global;

import java.util.Properties;

public class DbUtil {

    public static ConnectionInfo readConnectionInfo() {
        Properties properties = Global.getProperties();

        return new ConnectionInfo(
                properties.getProperty("dbUrl"),
                properties.getProperty("dbUser"),
                properties.getProperty("dbPassword"));
    }

}
