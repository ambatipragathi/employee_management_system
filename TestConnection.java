package employee_leave_management_system;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try {

            Connection con = ConnectionClass.getConnection();

            System.out.println("Connection successful!");

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}