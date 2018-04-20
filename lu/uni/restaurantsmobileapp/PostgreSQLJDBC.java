package lu.uni.restaurantsmobileapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQLJDBC {

    private static Connection connection;

    public static void main(String args[]) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://postgresql-unilu.alwaysdata.net/unilu_restaurants",
                            "unilu_jdbc", "unilujdbc");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static Connection getConnection() {
        return connection;
    }

}