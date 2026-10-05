package employee_leave_management_system;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class TransactionManagement {

    Connection con;
    Scanner sc;

    public TransactionManagement(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    // APPROVE LEAVE
    public void approveLeave() {

        try {

            System.out.print("\nEnter Leave ID to approve: ");
            int leaveId = Integer.parseInt(sc.nextLine());

            // Start transaction
            con.setAutoCommit(false);

            String selectSql =
                    "SELECT employee_id, from_date, to_date, status " +
                    "FROM leave_requests WHERE leave_id=?";

            PreparedStatement selectPs =
                    con.prepareStatement(selectSql);

            selectPs.setInt(1, leaveId);

            ResultSet rs = selectPs.executeQuery();

            if (!rs.next()) {

                System.out.println("Leave request not found!");

                con.rollback();
                con.setAutoCommit(true);
                return;
            }

            int employeeId = rs.getInt("employee_id");

            String status = rs.getString("status");

            if (!status.equals("PENDING")) {

                System.out.println(
                        "This leave request is already processed!"
                );

                con.rollback();
                con.setAutoCommit(true);
                return;
            }

            LocalDate fromDate =
                    rs.getDate("from_date").toLocalDate();

            LocalDate toDate =
                    rs.getDate("to_date").toLocalDate();

            long days =
                    ChronoUnit.DAYS.between(
                            fromDate,
                            toDate
                    ) + 1;

            // Get leave balance
            String balanceSql =
                    "SELECT used_leaves, remaining_leaves " +
                    "FROM leave_balance WHERE employee_id=?";

            PreparedStatement balancePs =
                    con.prepareStatement(balanceSql);

            balancePs.setInt(1, employeeId);

            ResultSet balanceRs =
                    balancePs.executeQuery();

            if (!balanceRs.next()) {

                System.out.println(
                        "Leave balance not found!"
                );

                con.rollback();
                con.setAutoCommit(true);
                return;
            }

            int usedLeaves =
                    balanceRs.getInt("used_leaves");

            int remainingLeaves =
                    balanceRs.getInt("remaining_leaves");

            // Check balance
            if (days > remainingLeaves) {

                System.out.println(
                        "Insufficient leave balance!"
                );

                con.rollback();
                con.setAutoCommit(true);
                return;
            }

            // Update leave request
            String updateLeaveSql =
                    "UPDATE leave_requests " +
                    "SET status='APPROVED' " +
                    "WHERE leave_id=?";

            PreparedStatement updateLeavePs =
                    con.prepareStatement(updateLeaveSql);

            updateLeavePs.setInt(1, leaveId);

            updateLeavePs.executeUpdate();

            // Update leave balance
            String updateBalanceSql =
                    "UPDATE leave_balance " +
                    "SET used_leaves=?, remaining_leaves=? " +
                    "WHERE employee_id=?";

            PreparedStatement updateBalancePs =
                    con.prepareStatement(updateBalanceSql);

            updateBalancePs.setInt(
                    1,
                    usedLeaves + (int) days
            );

            updateBalancePs.setInt(
                    2,
                    remainingLeaves - (int) days
            );

            updateBalancePs.setInt(
                    3,
                    employeeId
            );

            updateBalancePs.executeUpdate();

            // Commit
            con.commit();

            con.setAutoCommit(true);

            System.out.println(
                    "\nLeave approved successfully!"
            );

            System.out.println(
                    "Leaves deducted: " + days
            );

        } catch (Exception e) {

            try {

                con.rollback();
                con.setAutoCommit(true);

            } catch (Exception rollbackError) {

                rollbackError.printStackTrace();
            }

            e.printStackTrace();
        }
    }


    // REJECT LEAVE
    public void rejectLeave() {

        try {

            System.out.print("\nEnter Leave ID to reject: ");
            int leaveId = Integer.parseInt(sc.nextLine());

            String sql =
                    "UPDATE leave_requests " +
                    "SET status='REJECTED' " +
                    "WHERE leave_id=? AND status='PENDING'";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, leaveId);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                System.out.println(
                        "Leave rejected successfully!"
                );

            } else {

                System.out.println(
                        "Leave request not found or already processed!"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}