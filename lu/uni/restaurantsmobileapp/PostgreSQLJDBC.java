package lu.uni.restaurantsmobileapp;

import java.sql.*;
import java.util.HashMap;

public class PostgreSQLJDBC {

    private static Connection connection;

    /**
     * Open connection to the main database
     */
    public static void openConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager
                    .getConnection("jdbc:postgresql://postgresql-unilu.alwaysdata.net/unilu_restaurantmobileapp",
                            "unilu_jdbc", "unilujdbc");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Connection> Opened database successfully");
    }


    /**
     * Close connection after it has been used
     */
    public static void closeConnection() {
        try {
            getConnection().close();
            System.out.println("Connection> Successfully closed connection");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection> Unable to close connection");
        }
    }

    /**
     * Get the database connection
     * @return
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Insert data contained in a map into table
     * @param table
     * @param map <Column Name, Value>
     * @return true if inserted
     */
    public static boolean insert(String table, HashMap<String, Object> map) {
        String sql = "INSERT INTO " + table + " (";

        /*
        Add columns into SQL request
         */
        for (int i = 0; i < map.keySet().size(); i++) {
            sql += "?";
            if (i != (map.keySet().size() -1)) {
                sql += ",";
            }
        }


        sql += ") VALUES(";

        for (int i = 0; i < map.values().size(); i++) {
            sql += map.get(map.keySet().toArray()[i]).toString();
            if (i != (map.size() -1)) {
                sql += ",";
            }
        }



        PreparedStatement statement;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Check if restaurant is already added to database
     * @param name
     * @return
     */
    public static int exists(String name) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT id WHERE name=?");
            statement.setObject(1, name, Types.VARCHAR);
            ResultSet rs = statement.executeQuery();
            if (rs != null) {
                System.out.println("CheckDuplicate> Restaurant with name '" + name + "' already exists");
                return rs.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("CheckDuplicate> Unable to check if restaurant already exists");
        }
        return -1;
    }

}