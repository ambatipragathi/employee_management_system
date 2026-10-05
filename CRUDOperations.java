package employee_leave_management_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class CRUDOperations {

    Connection con;
    Scanner sc;

    public CRUDOperations(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    // 1. Employee Registration
    public void registerEmployee() {

        try {

            System.out.println("\n--- Employee Registration ---");

            System.out.print("Enter employee name: ");
            String name = sc.nextLine();

            System.out.print("Enter employee email: ");
            String email = sc.nextLine();

            System.out.print("Enter department: ");
            String department = sc.nextLine();

            String sql =
                    "INSERT INTO employees " +
                    "(employee_name, employee_email, department) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(
                    sql,
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, department);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            int employeeId = 0;

            if (rs.next()) {
                employeeId = rs.getInt(1);
            }

            // Create leave balance
            String balanceSql =
                    "INSERT INTO leave_balance " +
                    "(employee_id, total_leaves, used_leaves, remaining_leaves) " +
                    "VALUES (?, 12, 0, 12)";

            PreparedStatement balancePs =
                    con.prepareStatement(balanceSql);

            balancePs.setInt(1, employeeId);
            balancePs.executeUpdate();

            System.out.println(
                    "Employee registered successfully!"
            );

            System.out.println(
                    "Employee ID: " + employeeId
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 2. View Employees
    public void viewEmployees() {

        try {

            String sql = "SELECT * FROM employees";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- Employee List ---");

            while (rs.next()) {

                System.out.println(
                        "ID: " +
                        rs.getInt("employee_id") +

                        " | Name: " +
                        rs.getString("employee_name") +

                        " | Email: " +
                        rs.getString("employee_email") +

                        " | Department: " +
                        rs.getString("department")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 3. Apply Leave
    public void applyLeave() {

        try {

            System.out.println("\n--- Apply Leave ---");

            System.out.print("Enter employee ID: ");
            int employeeId =
                    Integer.parseInt(sc.nextLine());

            System.out.print("Enter leave type: ");
            String leaveType = sc.nextLine();

            System.out.print("Enter from date (YYYY-MM-DD): ");
            LocalDate fromDate =
                    LocalDate.parse(sc.nextLine());

            System.out.print("Enter to date (YYYY-MM-DD): ");
            LocalDate toDate =
                    LocalDate.parse(sc.nextLine());

            System.out.print("Enter reason: ");
            String reason = sc.nextLine();

            long days =
                    ChronoUnit.DAYS.between(
                            fromDate,
                            toDate
                    ) + 1;

            if (days <= 0) {

                System.out.println(
                        "Invalid date range!"
                );

                return;
            }

            // Check employee
            String employeeSql =
                    "SELECT employee_id FROM employees " +
                    "WHERE employee_id=?";

            PreparedStatement employeePs =
                    con.prepareStatement(employeeSql);

            employeePs.setInt(1, employeeId);

            ResultSet employeeRs =
                    employeePs.executeQuery();

            if (!employeeRs.next()) {

                System.out.println(
                        "Employee not found!"
                );

                return;
            }

            // Insert leave request
            String sql =
                    "INSERT INTO leave_requests " +
                    "(employee_id, leave_type, from_date, " +
                    "to_date, reason, status) " +
                    "VALUES (?, ?, ?, ?, ?, 'PENDING')";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, employeeId);
            ps.setString(2, leaveType);
            ps.setDate(3, Date.valueOf(fromDate));
            ps.setDate(4, Date.valueOf(toDate));
            ps.setString(5, reason);

            ps.executeUpdate();

            System.out.println(
                    "Leave applied successfully!"
            );

            System.out.println(
                    "Number of days: " + days
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 4. View Leave Requests
    public void viewLeaveRequests() {

        try {

            String sql =
                    "SELECT lr.leave_id, " +
                    "e.employee_name, " +
                    "lr.leave_type, " +
                    "lr.from_date, " +
                    "lr.to_date, " +
                    "lr.reason, " +
                    "lr.status " +
                    "FROM leave_requests lr " +
                    "JOIN employees e " +
                    "ON lr.employee_id = e.employee_id";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println(
                    "\n--- Leave Requests ---"
            );

            while (rs.next()) {

                System.out.println(
                        "Leave ID: " +
                        rs.getInt("leave_id") +

                        " | Employee: " +
                        rs.getString("employee_name") +

                        " | Type: " +
                        rs.getString("leave_type") +

                        " | From: " +
                        rs.getDate("from_date") +

                        " | To: " +
                        rs.getDate("to_date") +

                        " | Status: " +
                        rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 5. View Leave Balance
    public void viewLeaveBalance() {

        try {

            System.out.print(
                    "\nEnter employee ID: "
            );

            int employeeId =
                    Integer.parseInt(sc.nextLine());

            String sql =
                    "SELECT e.employee_name, " +
                    "lb.total_leaves, " +
                    "lb.used_leaves, " +
                    "lb.remaining_leaves " +
                    "FROM employees e " +
                    "JOIN leave_balance lb " +
                    "ON e.employee_id = lb.employee_id " +
                    "WHERE e.employee_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, employeeId);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                System.out.println(
                        "\nEmployee: " +
                        rs.getString("employee_name")
                );

                System.out.println(
                        "Total Leaves: " +
                        rs.getInt("total_leaves")
                );

                System.out.println(
                        "Used Leaves: " +
                        rs.getInt("used_leaves")
                );

                System.out.println(
                        "Remaining Leaves: " +
                        rs.getInt("remaining_leaves")
                );

            } else {

                System.out.println(
                        "Employee not found!"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 6. Update Employee
    public void updateEmployee() {

        try {

            System.out.print(
                    "\nEnter employee ID: "
            );

            int employeeId =
                    Integer.parseInt(sc.nextLine());

            System.out.print(
                    "Enter new email: "
            );

            String email = sc.nextLine();

            System.out.print(
                    "Enter new department: "
            );

            String department = sc.nextLine();

            String sql =
                    "UPDATE employees " +
                    "SET employee_email=?, department=? " +
                    "WHERE employee_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, department);
            ps.setInt(3, employeeId);

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Employee updated successfully!"
                );

            } else {

                System.out.println(
                        "Employee not found!"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 7. Delete Employee
    public void deleteEmployee() {

        try {

            System.out.print(
                    "\nEnter employee ID: "
            );

            int employeeId =
                    Integer.parseInt(sc.nextLine());

            String sql =
                    "DELETE FROM employees " +
                    "WHERE employee_id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, employeeId);

            int rows =
                    ps.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Employee deleted successfully!"
                );

            } else {

                System.out.println(
                        "Employee not found!"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Cannot delete employee if leave records exist."
            );
        }
    }
}