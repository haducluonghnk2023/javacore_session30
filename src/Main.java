import business.dao.login.LoginDAO;
import business.dao.login.LoginDAOImp;
import business.model.user.UserModel;
import presentation.DepartmentUI;
import presentation.EmployeeUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (!doLogin(sc)) {
            System.out.println("Sai tên đăng nhập hoặc mật khẩu. Vui lòng thử lại.\n");
        }
        displayMenu(sc);
    }

    public static boolean doLogin(Scanner sc) {
        LoginDAO loginDAO = new LoginDAOImp();
        System.out.println("********** ĐĂNG NHẬP **********");

        System.out.print("Tên đăng nhập: ");
        String username = sc.nextLine();

        System.out.print("Mật khẩu: ");
        String password = sc.nextLine();

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);

        boolean isLoggedIn = loginDAO.checkLogin(user);
        if (isLoggedIn) {
            System.out.println("Đăng nhập thành công!");
        } else {
            System.out.println("Sai tên đăng nhập hoặc mật khẩu.");
        }

        return isLoggedIn;
    }
    public static void displayMenu(Scanner sc) {
        do {
            System.out.println("****************MENNU******************");
            System.out.println("1. Quản lí phòng ban");
            System.out.println("2. Quản lí nhân viên");
            System.out.println("3. Đăng xuất");
            System.out.println("Lựa chọn của bạn: ");
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    DepartmentUI.displayDepartmentMenu(sc);
                    break;
                case 2:
                    EmployeeUI.displayEmployeeUI(sc);
                    break;
                case 3:
                    System.out.println("Bạn đã đăng xuất.\n");
                    while (!doLogin(sc)) {
                        System.out.println("Sai tên đăng nhập hoặc mật khẩu. Vui lòng thử lại.\n");
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại: ");
            }
        }while (true);
    }
}
