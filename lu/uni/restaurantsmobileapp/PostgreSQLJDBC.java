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
    public static boolean insert(String table, HashMap<String, SQLObject> map) {
        String sql = "INSERT INTO " + table + "(";

        /*
        Add columns into SQL request
         */
        for (int i = 0; i < map.keySet().size(); i++) {
            sql += map.keySet().toArray()[i];
            if (i != (map.keySet().size() -1)) {
                sql += ",";
            }
        }


        sql += ") VALUES(";


        /*
        Add ? for values
         */
        for (int i = 0; i < map.values().size(); i++) {
            sql += "?";
            if (i != (map.size() -1)) {
                sql += ",";
            }
        }
        sql += ")";



        PreparedStatement statement;
        try {
            statement = getConnection().prepareStatement(sql);

            /*
            Set values and types
             */
            for (int i = 1; i <= map.values().size(); i++) {
                statement.setObject(i, map.get(map.keySet().toArray()[i-1]).getObject(), map.get(map.keySet().toArray()[i-1]).getType());
            }

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
     * @return -1 if doesn't exist or id
     */
    public static int restaurantExists(String name) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT restaurantid FROM public.restaurant WHERE restaurantname=?");
            statement.setObject(1, name, Types.VARCHAR);
            ResultSet rs = statement.executeQuery();
            if (rs.next() == true) {
                System.out.println("CheckDuplicate> Restaurant with name '" + name + "' already exists");
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("CheckDuplicate> Unable to check if restaurant already exists");
        }
        return -1;
    }

    /**
     * Return values of row
     * @param id
     * @return Map<Column Name, Value>
     */
    public static HashMap<String, Object> getRestaurant(int id) {
        HashMap<String, Object> map = new HashMap <>();
        String sql = "SELECT * FROM public.restaurant WHERE restaurantid=?";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, id, Types.NUMERIC);
            ResultSet res = statement.executeQuery();


            while (res.next()) {
                map.put("category", res.getString("category"));
                map.put("meal", res.getString("meal"));
                map.put("cuisine", res.getString("cuisine"));
                map.put("todaymenu", res.getBoolean("todaymenu"));
                map.put("price", res.getString("price"));
                map.put("wifi", res.getBoolean("wifi"));
                map.put("menuonwebsite", res.getBoolean("menuonwebsite"));
                map.put("rating", res.getDouble("rating"));
                map.put("facebook", res.getBoolean("facebook"));
                map.put("menuonfb", res.getBoolean("menuonfb"));
                map.put("fbmenu", res.getString("fbmenu"));
                map.put("fbpage", res.getString("fbpage"));
                map.put("hasnewsletter", res.getBoolean("hasnewsletter"));
                map.put("newsletter", res.getString("newsletter"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Get location values
     * @param id LOCATIONID
     * @return
     */
    public static HashMap<String, Object> getLocation(int id) {
        HashMap<String, Object> map = new HashMap <>();
        String sql = "SELECT * FROM public.location WHERE locationid=?";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, id, Types.NUMERIC);
            ResultSet res = statement.executeQuery();


            if (res.next()) {
                map.put("streetnumber", res.getString("streetnumber"));
                map.put("streetname", res.getString("streetname"));
                map.put("city", res.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Get GPS position values
     * @param id GPSPOSITIONID
     * @return
     */
    public static HashMap<String, Object> getGPSPosition(int id) {

        HashMap<String, Object> map = new HashMap <>();
        String sql = "SELECT * FROM public.gpsposition WHERE gpspositionid=?";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, id, Types.NUMERIC);
            ResultSet res = statement.executeQuery();


            if (res.next()) {
                map.put("longitude", res.getString("longitude"));
                map.put("latitude", res.getString("latitude"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Get location id from a restaurant id
     * @param restaurantId
     * @return locationid
     */
    public static int getLocationId(int restaurantId) {
        HashMap<String, Object> map = new HashMap <>();
        String sql = "SELECT locationid FROM public.restaurant WHERE restaurantid=?";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, restaurantId, Types.NUMERIC);
            ResultSet res = statement.executeQuery();


            if (res.next()) {
                return  res.getInt("locationid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get gps position id from a restaurant id
     * @param restaurantId
     * @return gpspositionid
     */
    public static int getPositionId(int restaurantId) {
        HashMap<String, Object> map = new HashMap <>();
        String sql = "SELECT gpspositionid FROM public.restaurant WHERE restaurantid=?";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, restaurantId, Types.NUMERIC);
            ResultSet res = statement.executeQuery();


            if (res.next()) {
                return  res.getInt("gpspositionid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Check if location already exist and get its id
     * @param streetNumber
     * @param streetName
     * @param city
     * @return
     */
    public static int askForLocationId(String streetNumber, String streetName, String city) {
        String sql = "SELECT locationid FROM public.location WHERE streetnumber=? and streetname=? and city=?";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, streetNumber, Types.VARCHAR);
            statement.setObject(2, streetName, Types.VARCHAR);
            statement.setObject(3, city, Types.VARCHAR);
            ResultSet res = statement.executeQuery();


            if (res.next()) {
                return res.getInt("locationid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HashMap<String, SQLObject> map = new HashMap <>();
        map.put("streetnumber", new SQLObject(streetNumber, Types.VARCHAR));
        map.put("streetname", new SQLObject(streetName, Types.VARCHAR));
        map.put("city", new SQLObject(city, Types.VARCHAR));
        insert("location", map);
        return askForLocationId(streetNumber, streetName, city);
    }


    /**
     * Check if position already exist and get its id
     * @param longitude
     * @param latitude
     * @return
     */
    public static int askForPositionId(String longitude, String latitude) {
        String sql = "SELECT gpspositionid FROM public.gpsposition WHERE longitude=? and latitude=?";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, longitude, Types.VARCHAR);
            statement.setObject(2, latitude, Types.VARCHAR);
            ResultSet res = statement.executeQuery();


            if (res.next()) {
                return res.getInt("gpspositionid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HashMap<String, SQLObject> map = new HashMap <>();
        map.put("longitude", new SQLObject(longitude, Types.VARCHAR));
        map.put("latitude", new SQLObject(latitude, Types.VARCHAR));
        insert("gpsposition", map);
        return askForPositionId(longitude, latitude);
    }

    public static void update(String table, String colname, SQLObject obj, String where, int id) {
        String sql = "UPDATE " + table + " SET " + colname + "=?" + " WHERE " + where + "=" + id;
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(sql);
            statement.setObject(1, obj.getObject(), obj.getType());
            System.out.println(statement);
            System.out.println(obj.getType());
            int c = statement.executeUpdate();
            System.out.println("Database> Successfully updated '" + colname + "' where id=" + id + " ; " + c +" lines changed");

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}