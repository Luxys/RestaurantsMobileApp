package lu.uni.restaurantsmobileapp;

import java.io.File;

public class RestaurantsMobileApp {

    public static void main(String[] args) {
        PostgreSQLJDBC.openConnection();
        File file = new File("C:\\Users\\lucas\\Documents\\RestaurantsMobileApp\\DA.xlsx");
        ExcelManagement.readSheet(file);
        PostgreSQLJDBC.closeConnection();
    }


}
