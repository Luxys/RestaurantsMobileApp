package lu.uni.restaurantsmobileapp;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.sql.Types;
import java.util.HashMap;

public class ExcelManagement {

    public static void readSheet(File file) {
        try {

           /*
           Load Excel Sheet
            */
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            Row row;
            Cell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }


            /*
            Read cells
             */
            for(int r = 3; r < 20; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    String name = null;
                    String category = null;
                    String meal = null;
                    String cuisine = null;
                    boolean todayMenu = false;
                    String location = null;
                    String price = null;
                    boolean wifi = false;
                    String phone = null;
                    String mail = null;
                    boolean menuOnWebsite = false;
                    boolean haveDailyMenu = false;
                    String website = null;
                    double rating = 0;
                    boolean facebook = false;
                    boolean menuOnFB = false;
                    String fbMenu = null;
                    String fbPage = null;
                    for(int c = 0; c < cols; c++) {
                        cell = row.getCell((short)c);
                        if(cell != null) switch (c) {
                            case 0:
                                name = cell.getStringCellValue();
                                break;
                            case 1:
                                category = cell.getStringCellValue();
                                break;
                            case 2:
                                meal = cell.getStringCellValue();
                                break;
                            case 3:
                                cuisine = cell.getStringCellValue();
                                break;
                            case 4:
                                todayMenu = cell.getBooleanCellValue();
                                break;
                            case 5:
                                location = cell.getStringCellValue();
                                break;
                            case 6:
                                price = cell.getStringCellValue();
                                break;
                            case 7:
                                wifi = cell.getBooleanCellValue();
                                break;
                            case 8:
                                phone = cell.getStringCellValue();
                                break;
                            case 9:
                                mail = cell.getStringCellValue();
                                break;
                            case 10:
                                menuOnWebsite = cell.getBooleanCellValue();
                                break;
                            case 11:
                                haveDailyMenu = cell.getBooleanCellValue();
                                break;
                            case 12:
                                website = cell.getStringCellValue();
                                break;
                            case 13:
                                rating = cell.getNumericCellValue();
                                break;
                            case 14:
                                facebook = cell.getBooleanCellValue();
                                break;
                            case 15:
                                menuOnFB = cell.getBooleanCellValue();
                                break;
                            case 16:
                                fbMenu = cell.getStringCellValue();
                            case 17:
                                fbPage = cell.getStringCellValue();
                                break;

                        }
                    }

                    // Does Restaurant already exist?
                    int id = PostgreSQLJDBC.restaurantExists(name);
                    if (id == -1) {

                        /*
                        Insert new restaurant
                         */
                        HashMap<String, SQLObject> map = new HashMap <>();
                        map.put("restaurantname", new SQLObject(name, Types.VARCHAR));
                        map.put("category", new SQLObject(category, Types.VARCHAR));
                        map.put("meal", new SQLObject(meal, Types.VARCHAR));
                        map.put("cuisine", new SQLObject(cuisine, Types.VARCHAR));
                        map.put("todaymenu", new SQLObject(todayMenu, Types.BOOLEAN));
                        map.put("price", new SQLObject(price, Types.VARCHAR));
                        map.put("wifi", new SQLObject(wifi, Types.BOOLEAN));
                        map.put("menuonwebsite", new SQLObject(menuOnWebsite, Types.BOOLEAN));
                        map.put("havedailymenu", new SQLObject(haveDailyMenu, Types.BOOLEAN));
                        map.put("rating", new SQLObject(rating, Types.NUMERIC));
                        map.put("facebook", new SQLObject(facebook, Types.BOOLEAN));
                        map.put("menuonfb", new SQLObject(menuOnFB , Types.BOOLEAN));
                        map.put("fbmenu", new SQLObject(fbMenu, Types.VARCHAR));
                        map.put("fbpage", new SQLObject(fbPage, Types.VARCHAR));
                        PostgreSQLJDBC.insert("restaurant", map);
                        System.out.println("Database> Successfully added new restaurant " + name.toUpperCase());



                    } else {

                        /*
                        Restaurant already exist: update?
                         */
                        HashMap<String, Object> map = PostgreSQLJDBC.getRestaurant(id);
                        if (!((String) map.get("category")).equalsIgnoreCase(category)) {
                            PostgreSQLJDBC.update("public.restaurant", "category", new SQLObject(category, Types.VARCHAR), "restaurantid", id);
                        }
                        if (!((String) map.get("meal")).equalsIgnoreCase(meal)) {
                            PostgreSQLJDBC.update("public.restaurant", "meal", new SQLObject(meal, Types.VARCHAR), "restaurantid", id);
                        }
                        if (!((String) map.get("cuisine")).equalsIgnoreCase(cuisine)) {
                            PostgreSQLJDBC.update("public.restaurant", "cuisine", new SQLObject(cuisine, Types.VARCHAR), "restaurantid", id);
                        }
                        if (!((String) map.get("price")).equalsIgnoreCase(price)) {
                            PostgreSQLJDBC.update("public.restaurant", "price", new SQLObject(price, Types.VARCHAR), "restaurantid", id);
                        }
                        if (!((String) map.get("fbmenu")).equalsIgnoreCase(fbMenu)) {
                            PostgreSQLJDBC.update("public.restaurant", "fbmenu", new SQLObject(fbMenu, Types.VARCHAR), "restaurantid", id);
                        }
                        if (!((String) map.get("fbpage")).equalsIgnoreCase(fbPage)) {
                            PostgreSQLJDBC.update("public.restaurant", "fbpage", new SQLObject(fbPage, Types.VARCHAR), "restaurantid", id);
                        }
                        if (!((boolean) PostgreSQLJDBC.getRestaurant(id).get("wifi") == wifi)) {
                            PostgreSQLJDBC.update("public.restaurant", "wifi", new SQLObject(wifi, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((boolean) PostgreSQLJDBC.getRestaurant(id).get("todaymenu") == todayMenu)) {
                            PostgreSQLJDBC.update("public.restaurant", "todaymenu", new SQLObject(todayMenu, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((boolean) PostgreSQLJDBC.getRestaurant(id).get("facebook") == facebook)) {
                            PostgreSQLJDBC.update("public.restaurant", "facebook", new SQLObject(facebook, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((boolean) PostgreSQLJDBC.getRestaurant(id).get("menuonfb") == menuOnFB)) {
                            PostgreSQLJDBC.update("public.restaurant", "menuonfb", new SQLObject(menuOnFB, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((double) PostgreSQLJDBC.getRestaurant(id).get("rating") == rating)) {
                            PostgreSQLJDBC.update("public.restaurant", "rating", new SQLObject(rating, Types.NUMERIC), "restaurantid", id);
                        }


                    }



                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }


}

