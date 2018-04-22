package lu.uni.restaurantsmobileapp;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.Types;

public class ExcelManagement {

    public static void readSheet(File file) {
        try {
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

                    //test
                    PreparedStatement statement = PostgreSQLJDBC.getConnection().prepareStatement("INSERT INTO" +
                            " restaurant  (restaurantname, category, meal, cuisine, todayMenu, price, wifi," +
                            " menuOnWebsite, haveDailyMenu, rating, facebook, menuOnFb, fbMenu, fbPage) " +
                            " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                    statement.setObject(1, name, Types.VARCHAR);
                    statement.setObject(2, category, Types.VARCHAR);
                    statement.setObject(3, meal, Types.VARCHAR);
                    statement.setObject(4, cuisine, Types.VARCHAR);
                    statement.setObject(5, todayMenu, Types.BOOLEAN);
                    statement.setObject(6, price, Types.VARCHAR);
                    statement.setObject(7, wifi, Types.BOOLEAN);
                    statement.setObject(8, menuOnWebsite, Types.BOOLEAN);
                    statement.setObject(9, haveDailyMenu, Types.BOOLEAN);
                    statement.setObject(10, rating, Types.DOUBLE);
                    statement.setObject(11, facebook, Types.BOOLEAN);
                    statement.setObject(12, menuOnFB, Types.BOOLEAN);
                    statement.setObject(13, fbMenu, Types.VARCHAR);
                    statement.setObject(14, fbPage, Types.VARCHAR);

                    statement.executeUpdate();
                    System.out.println("Database> Updated table 'restaurant' successfully");
                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }


}

