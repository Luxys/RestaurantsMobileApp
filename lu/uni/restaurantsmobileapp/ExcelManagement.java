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
            for(int r = 2; r < 20; r++) {
                row = sheet.getRow(r);
                if(row != null) {

                    /*
                    Location
                     */
                    String streetNumber = null;
                    String streetName = null;
                    String city = null;
                    String longitude = null;
                    String latitude = null;

                    /*
                    Contact info
                     */
                    boolean wifi = false;
                    String phone = null;
                    String mail = null;
                    String website = null;

                    String name = null;
                    String price = null;
                    String category = null;
                    String meal = null;
                    String cuisine = null;
                    boolean todayMenu = false;
                    boolean menuOnWebsite = false;
                    boolean haveDailyMenu = false;
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
                                streetNumber = cell.getStringCellValue();
                                break;
                            case 6:
                                streetName = cell.getStringCellValue();
                                break;
                            case 7:
                                city = cell.getStringCellValue();
                                break;
                            case 8:
                                longitude = cell.getStringCellValue();
                                break;
                            case 9:
                                latitude = cell.getStringCellValue();
                                break;
                            case 10:
                                price = cell.getStringCellValue();
                                break;
                            case 11:
                                wifi = cell.getBooleanCellValue();
                                break;
                            case 12:
                                phone = cell.getStringCellValue();
                                break;
                            case 13:
                                mail = cell.getStringCellValue();
                                break;
                            case 14:
                                menuOnWebsite = cell.getBooleanCellValue();
                                break;
                            case 15:
                                haveDailyMenu = cell.getBooleanCellValue();
                                break;
                            case 16:
                                website = cell.getStringCellValue();
                                break;
                            case 17:
                                rating = cell.getNumericCellValue();
                                break;
                            case 18:
                                facebook = cell.getBooleanCellValue();
                                break;
                            case 19:
                                menuOnFB = cell.getBooleanCellValue();
                                break;
                            case 20:
                                fbMenu = cell.getStringCellValue();
                            case 21:
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

                        /*
                        Update ID
                         */
                        id = PostgreSQLJDBC.restaurantExists(name);

                        /*
                        Insert new location
                         */
                        int locationId = PostgreSQLJDBC.askForLocationId(streetNumber, streetName, city);

                        /*
                        Insert new GPS position
                         */
                        int gpsId = PostgreSQLJDBC.askForPositionId(longitude, latitude);
                        /*
                        Update infos
                         */
                        PostgreSQLJDBC.update("restaurant", "gpspositionid", new SQLObject(gpsId, Types.NUMERIC), "restaurantid", id);
                        PostgreSQLJDBC.update("restaurant", "locationid", new SQLObject(locationId, Types.NUMERIC), "restaurantid", id);



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
                        if (!((boolean) map.get("wifi") == wifi)) {
                            PostgreSQLJDBC.update("public.restaurant", "wifi", new SQLObject(wifi, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((boolean) map.get("todaymenu") == todayMenu)) {
                            PostgreSQLJDBC.update("public.restaurant", "todaymenu", new SQLObject(todayMenu, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((boolean) map.get("facebook") == facebook)) {
                            PostgreSQLJDBC.update("public.restaurant", "facebook", new SQLObject(facebook, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((boolean) map.get("menuonfb") == menuOnFB)) {
                            PostgreSQLJDBC.update("public.restaurant", "menuonfb", new SQLObject(menuOnFB, Types.BOOLEAN), "restaurantid", id);
                        }
                        if (!((double) map.get("rating") == rating)) {
                            PostgreSQLJDBC.update("public.restaurant", "rating", new SQLObject(rating, Types.NUMERIC), "restaurantid", id);
                        }



                        /*
                        Location update?
                         */
                        int locationId = PostgreSQLJDBC.getLocationId(id);
                        HashMap<String, Object> loc = PostgreSQLJDBC.getLocation(locationId);
                        if (!((String) loc.get("streetname")).equalsIgnoreCase(streetName)) {
                            PostgreSQLJDBC.update("public.location", "streetname", new SQLObject(streetName, Types.VARCHAR), "locationid", locationId);
                        }
                        if (!((String) loc.get("streetnumber")).equalsIgnoreCase(streetNumber)) {
                            PostgreSQLJDBC.update("public.location", "streetnumber", new SQLObject(streetNumber, Types.VARCHAR), "locationid", locationId);
                        }
                        if (!((String) loc.get("city")).equalsIgnoreCase(city)) {
                            PostgreSQLJDBC.update("public.location", "city", new SQLObject(city, Types.VARCHAR), "locationid", locationId);
                        }


                         /*
                        GPS position update?
                         */
                         int gpsId = PostgreSQLJDBC.getPositionId(id);
                        HashMap<String, Object> gps = PostgreSQLJDBC.getGPSPosition(gpsId);
                        if (!((String) gps.get("longitude")).equalsIgnoreCase(longitude)) {
                            PostgreSQLJDBC.update("public.gpsposition", "longitude", new SQLObject(longitude, Types.VARCHAR), "gpspositionid", gpsId);
                        }
                        if (!((String) gps.get("latitude")).equalsIgnoreCase(latitude)) {
                            PostgreSQLJDBC.update("public.gpsposition", "latitude", new SQLObject(latitude, Types.VARCHAR), "gpspositionid", gpsId);
                        }

                    }



                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }





}

