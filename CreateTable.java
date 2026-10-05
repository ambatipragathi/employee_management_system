package employee_leave_management_system;

import java.sql.Connection;
import java.sql.Statement;

public class CreateTable {

    Connection con;

    public CreateTable(Connection con) {
        this.con = con;
    }

    // Create employees table
    public void createEmployeesTable() throws Exception {

        String sql = "CREATE TABLE IF NOT EXISTS employees ("
                + "employee_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "employee_name VARCHAR(100) NOT NULL,"
                + "employee_email VARCHAR(100) NOT NULL,"
                + "department VARCHAR(50) NOT NULL"
                + ")";

        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);

        System.out.println("Employees table created successfully.");
    }


    // Create leave_requests table
    public void createLeaveRequestsTable() throws Exception {

        String sql = "CREATE TABLE IF NOT EXISTS leave_requests ("
                + "leave_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "employee_id INT NOT NULL,"
                + "leave_type VARCHAR(50) NOT NULL,"
                + "from_date DATE NOT NULL,"
                + "to_date DATE NOT NULL,"
                + "reason VARCHAR(255),"
                + "status VARCHAR(20) DEFAULT 'PENDING',"
                + "FOREIGN KEY (employee_id) REFERENCES employees(employee_id)"
                + ")";

        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);

        System.out.println("Leave requests table created successfully.");
    }


    // Create leave_balance table
    public void createLeaveBalanceTable() throws Exception {

        String sql = "CREATE TABLE IF NOT EXISTS leave_balance ("
                + "balance_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "employee_id INT NOT NULL,"
                + "total_leaves INT DEFAULT 12,"
                + "used_leaves INT DEFAULT 0,"
                + "remaining_leaves INT DEFAULT 12,"
                + "FOREIGN KEY (employee_id) REFERENCES employees(employee_id)"
                + ")";

        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);

        System.out.println("Leave balance table created successfully.");
    }
}