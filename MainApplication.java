package employee_leave_management_system;

import java.sql.Connection;

public class MainApplication {

    public static void main(String[] args) {

        Connection con = null;

        try {

            // Connect to MySQL
            con = ConnectionClass.getConnection();

            System.out.println("======================================");
            System.out.println(" Database connected successfully!");
            System.out.println("======================================");

            // Create tables using JDBC
            CreateTable createTable = new CreateTable(con);

            createTable.createEmployeesTable();
            createTable.createLeaveRequestsTable();
            createTable.createLeaveBalanceTable();

            System.out.println("All tables are ready!");

            // Start application menu
            Menu menu = new Menu(con);

            menu.showMenu();

        } catch (Exception e) {

            System.out.println("Something went wrong!");

            e.printStackTrace();

        } finally {

            try {

                if (con != null) {
                    con.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}