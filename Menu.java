package employee_leave_management_system;

import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    Connection con;
    Scanner sc;

    CRUDOperations crud;
    TransactionManagement transaction;

    public Menu(Connection con) {

        this.con = con;

        sc = new Scanner(System.in);

        crud = new CRUDOperations(con, sc);

        transaction = new TransactionManagement(con, sc);
    }

    public void showMenu() {

        while (true) {

            System.out.println("\n======================================");
            System.out.println("   EMPLOYEE LEAVE MANAGEMENT SYSTEM");
            System.out.println("======================================");

            System.out.println("1. Register Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Apply Leave");
            System.out.println("4. View Leave Requests");
            System.out.println("5. Approve Leave");
            System.out.println("6. Reject Leave");
            System.out.println("7. View Leave Balance");
            System.out.println("8. Update Employee");
            System.out.println("9. Delete Employee");
            System.out.println("10. Exit");

            System.out.print("\nEnter your choice: ");

            try {

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {

                    case 1:
                        crud.registerEmployee();
                        break;

                    case 2:
                        crud.viewEmployees();
                        break;

                    case 3:
                        crud.applyLeave();
                        break;

                    case 4:
                        crud.viewLeaveRequests();
                        break;

                    case 5:
                        transaction.approveLeave();
                        break;

                    case 6:
                        transaction.rejectLeave();
                        break;

                    case 7:
                        crud.viewLeaveBalance();
                        break;

                    case 8:
                        crud.updateEmployee();
                        break;

                    case 9:
                        crud.deleteEmployee();
                        break;

                    case 10:

                        System.out.println(
                                "\nThank you for using Employee Leave Management System!"
                        );

                        return;

                    default:

                        System.out.println(
                                "Invalid choice. Please try again."
                        );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}