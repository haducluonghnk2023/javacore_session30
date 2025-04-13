package presentation;

import business.model.employee.EmployeeModel;
import business.model.employee.Sex;
import business.model.employee.Status;
import business.service.employee.EmployeeServiceImp;
import business.util.PageInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class EmployeeUI {
    public static void displayEmployeeUI(Scanner scanner) {
        EmployeeServiceImp employeeService = new EmployeeServiceImp();
        boolean continueProgram = true;

        do {
            System.out.println("\n========== MENU NHÂN VIÊN ==========");
            System.out.println("1. Danh sách nhân viên có phân trang");
            System.out.println("2. Thêm mới nhân viên");
            System.out.println("3. Cập nhật nhân viên");
            System.out.println("4. Xóa nhân viên");
            System.out.println("5. Tìm kiếm nhân viên");
            System.out.println("6. Sắp xếp nhân viên");
            System.out.println("7. Thoát");
            System.out.print("Chọn chức năng: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> displayEmployeeList(scanner, employeeService);
                    case 2 -> addEmployee(scanner, employeeService);
                    case 3 -> updateEmployee(scanner, employeeService);
                    case 4 -> deleteEmployee(scanner, employeeService);
                    case 5 -> searchEmployeeMenu(scanner, employeeService);
                    case 6 -> sortEmployees(scanner, employeeService);
                    case 7 -> {
                        continueProgram = false;
                        System.out.println("Thoát chương trình...");
                    }
                    default -> System.out.println("\u001B[31mLựa chọn không hợp lệ!\u001B[0m");
                }
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mVui lòng nhập số!\u001B[0m");
            } catch (Exception e) {
                System.out.println("\u001B[31mĐã xảy ra lỗi: " + e.getMessage() + "\u001B[0m");
            }
        } while (continueProgram);
    }

    private static void displayEmployeeList(Scanner scanner, EmployeeServiceImp employeeService) {
        int currentPage = 1;
        int pageSize = 5;
        boolean continueList = true;

        while (continueList) {
            try {
                PageInfo<EmployeeModel> pageInfo = employeeService.getPageData(currentPage, pageSize);
                List<EmployeeModel> employees = pageInfo.getRecords();

                System.out.println("\u001B[36m\n--- Danh sách nhân viên (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " nhân viên\u001B[0m");
                if (employees.isEmpty()) {
                    System.out.println("\u001B[31mKhông có nhân viên nào ở trang này.\u001B[0m");
                } else {
                    for (EmployeeModel emp : employees) {
                        System.out.println(emp);
                    }
                }

                System.out.println("\u001B[36m\n== Tùy chọn điều hướng ==\u001B[0m");
                System.out.println("1. Trang tiếp");
                System.out.println("2. Trang trước");
                System.out.println("3. Đến trang cụ thể");
                System.out.println("4. Quay lại menu chính");
                System.out.print("Lựa chọn: ");

                try {
                    int choice = Integer.parseInt(scanner.nextLine());

                    switch (choice) {
                        case 1 -> {
                            if (currentPage < pageInfo.getTotalPages()) {
                                currentPage++;
                            } else {
                                System.out.println("\u001B[31mBạn đang ở trang cuối cùng.\u001B[0m");
                            }
                        }
                        case 2 -> {
                            if (currentPage > 1) {
                                currentPage--;
                            } else {
                                System.out.println("\u001B[31mBạn đang ở trang đầu tiên.\u001B[0m");
                            }
                        }
                        case 3 -> {
                            System.out.print("Nhập số trang (1 - " + pageInfo.getTotalPages() + "): ");
                            int targetPage = Integer.parseInt(scanner.nextLine());
                            if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                                currentPage = targetPage;
                            } else {
                                System.out.println("\u001B[31mSố trang không hợp lệ.\u001B[0m");
                            }
                        }
                        case 4 -> continueList = false;
                        default -> System.out.println("\u001B[31mLựa chọn không hợp lệ.\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mVui lòng nhập số!\u001B[0m");
                }

            } catch (Exception e) {
                System.out.println("\u001B[31mĐã xảy ra lỗi: " + e.getMessage() + "\u001B[0m");
                continueList = false;
            }
        }
    }

    private static void addEmployee(Scanner scanner, EmployeeServiceImp employeeService) {
        try {
            EmployeeModel newEmp = new EmployeeModel();
            newEmp.inputData(scanner);

            boolean isAdded = employeeService.add(newEmp);
            if (isAdded) {
                System.out.println("\u001B[32mThêm nhân viên thành công.\u001B[0m");
            } else {
                System.out.println("\u001B[31mThêm nhân viên thất bại.\u001B[0m");
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mĐã xảy ra lỗi khi thêm nhân viên: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void updateEmployee(Scanner scanner, EmployeeServiceImp employeeService) {
        try {
            System.out.print("Nhập ID nhân viên cần cập nhật: ");
            String employeeId = scanner.nextLine();

            EmployeeModel existingEmp = employeeService.findById(employeeId);
            if (existingEmp == null) {
                System.out.println("\u001B[31mKhông tìm thấy nhân viên với ID: " + employeeId + "\u001B[0m");
                return;
            }

            // Hiển thị menu cập nhật
            System.out.println("\u001B[36m\n== Chọn thông tin muốn cập nhật ==\u001B[0m");
            System.out.println("1. Tên nhân viên");
            System.out.println("2. Ngày sinh");
            System.out.println("3. Email");
            System.out.println("4. Số điện thoại");
            System.out.println("5. Địa chỉ");
            System.out.println("6. Phòng ban");
            System.out.println("7. Trạng thái");
            System.out.println("8. Giới tính");
            System.out.println("9. Lương");
            System.out.println("10. Hệ số lương");
            System.out.println("11. Cập nhật tất cả");
            System.out.print("Lựa chọn của bạn: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Nhập tên nhân viên mới: ");
                    existingEmp.setEmployeeName(scanner.nextLine());
                }
                case 2 -> {
                    System.out.print("Nhập ngày sinh mới (dd/MM/yyyy): ");
                    String dobInput = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dob = LocalDate.parse(dobInput, formatter);
                    existingEmp.setEmployeeBirthday(dob);
                }
                case 3 -> {
                    System.out.print("Nhập email mới: ");
                    existingEmp.setEmployeeEmail(scanner.nextLine());
                }
                case 4 -> {
                    System.out.print("Nhập số điện thoại mới: ");
                    existingEmp.setEmployeePhone(scanner.nextLine());
                }
                case 5 -> {
                    System.out.print("Nhập địa chỉ mới: ");
                    existingEmp.setEmployeeAddress(scanner.nextLine());
                }
                case 6 -> {
                    System.out.print("Nhập ID phòng ban mới: ");
                    existingEmp.setDepartmentId(Integer.parseInt(scanner.nextLine()));
                }
                case 7 -> {
                    System.out.print("Trạng thái (ACTIVE, INACTIVE, ONLEAVE, POLICYLEAVE): ");
                    String statusInput = scanner.nextLine().toUpperCase();
                    try {
                        existingEmp.setEmployeeStatus(Status.valueOf(statusInput));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Trạng thái không hợp lệ! Vui lòng nhập lại.");
                        return;
                    }
                }
                case 8 -> {
                    System.out.print("Giới tính (MALE, FEMALE, OTHER): ");
                    String sexInput = scanner.nextLine().toUpperCase();
                    try {
                        existingEmp.setEmployeeSex(Sex.valueOf(sexInput));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Giới tính không hợp lệ! Vui lòng nhập lại.");
                        return;
                    }
                }
                case 9 -> {
                    System.out.print("Nhập lương mới: ");
                    existingEmp.setEmployeeSalary(Double.parseDouble(scanner.nextLine()));
                }
                case 10 -> {
                    System.out.print("Nhập cấp bậc lương mới: ");
                    existingEmp.setEmployeeSalaryLevel(Double.parseDouble(scanner.nextLine()));
                }
                case 11 -> {
                    System.out.print("Nhập tên nhân viên mới: ");
                    existingEmp.setEmployeeName(scanner.nextLine());

                    System.out.print("Nhập ngày sinh mới (dd/MM/yyyy): ");
                    String dobInput = scanner.nextLine();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    existingEmp.setEmployeeBirthday(LocalDate.parse(dobInput, formatter));

                    System.out.print("Nhập email mới: ");
                    existingEmp.setEmployeeEmail(scanner.nextLine());

                    System.out.print("Nhập số điện thoại mới: ");
                    existingEmp.setEmployeePhone(scanner.nextLine());

                    System.out.print("Nhập địa chỉ mới: ");
                    existingEmp.setEmployeeAddress(scanner.nextLine());

                    System.out.print("Nhập ID phòng ban mới: ");
                    existingEmp.setDepartmentId(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Trạng thái (ACTIVE, INACTIVE, ONLEAVE, POLICYLEAVE): ");
                    existingEmp.setEmployeeStatus(Status.valueOf(scanner.nextLine().toUpperCase()));

                    System.out.print("Giới tính (MALE, FEMALE, OTHER): ");
                    existingEmp.setEmployeeSex(Sex.valueOf(scanner.nextLine().toUpperCase()));

                    System.out.print("Nhập lương mới: ");
                    existingEmp.setEmployeeSalary(Double.parseDouble(scanner.nextLine()));

                    System.out.print("Nhập hệ cấp bậc lương mới: ");
                    existingEmp.setEmployeeSalaryLevel(Double.parseDouble(scanner.nextLine()));
                }
            }

            // Gọi update
            boolean isUpdated = employeeService.update(existingEmp);
            if (isUpdated) {
                System.out.println("\u001B[32mCập nhật nhân viên thành công.\u001B[0m");
            } else {
                System.out.println("\u001B[31mCập nhật nhân viên thất bại.\u001B[0m");
            }

        } catch (Exception e) {
            System.out.println("\u001B[31mĐã xảy ra lỗi khi cập nhật nhân viên: " + e.getMessage() + "\u001B[0m");
            e.printStackTrace();
        }
    }



    private static void deleteEmployee(Scanner scanner, EmployeeServiceImp employeeService) {
        try {
            System.out.print("Nhập ID nhân viên cần xóa: ");
            String employeeId = scanner.nextLine();

            String message = employeeService.delete(employeeId);
            if (message.contains("successfully")) {
                System.out.println("\u001B[32m" + message + "\u001B[0m");
            } else if (message.contains("Cannot delete")) {
                System.out.println("\u001B[31m" + message + "\u001B[0m");
            } else if (message.contains("does not exist")) {
                System.out.println("\u001B[33m" + message + "\u001B[0m");
            } else {
                System.out.println(message);
            }
        } catch (NumberFormatException e) {
            System.out.println("\u001B[31mVui lòng nhập ID hợp lệ!\u001B[0m");
        } catch (Exception e) {
            System.out.println("\u001B[31mĐã xảy ra lỗi khi xóa nhân viên: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void searchEmployeeMenu(Scanner scanner, EmployeeServiceImp employeeService) {
        while (true) {
            System.out.println("\n=== Menu Tìm Kiếm ===");
            System.out.println("1. Tìm kiếm theo tên");
            System.out.println("2. Tìm kiếm theo khoảng tuổi");
            System.out.println("3. Thoát");
            System.out.print("Chọn phương thức tìm kiếm: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    searchEmployeeByName(scanner, employeeService);
                    break;
                case 2:
                    searchEmployeeByAgeRange(scanner, employeeService);
                    break;
                case 3:
                    System.out.println("Thoát khỏi menu tìm kiếm.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        }
    }

    private static void searchEmployeeByName(Scanner scanner, EmployeeServiceImp employeeService) {
        System.out.print("Nhập tên nhân viên cần tìm kiếm: ");
        String keyword = scanner.nextLine();

        try {
            List<EmployeeModel> results = employeeService.searchByName(keyword);
            if (results.isEmpty()) {
                System.out.println("\u001B[31mKhông tìm thấy nhân viên nào với tên: " + keyword + "\u001B[0m");
            } else {
                System.out.println("\u001B[36m\n== Kết quả tìm kiếm ==\u001B[0m");
                for (EmployeeModel emp : results) {
                    System.out.println(emp);
                }
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mĐã xảy ra lỗi khi tìm kiếm nhân viên: " + e.getMessage() + "\u001B[0m");
        }
    }

    private static void searchEmployeeByAgeRange(Scanner scanner, EmployeeServiceImp employeeService) {
        System.out.print("Nhập độ tuổi tối thiểu: ");
        int minAge = Integer.parseInt(scanner.nextLine());

        System.out.print("Nhập độ tuổi tối đa: ");
        int maxAge = Integer.parseInt(scanner.nextLine());

        try {
            List<EmployeeModel> results = employeeService.searchByAgeRange(minAge, maxAge);
            if (results.isEmpty()) {
                System.out.println("\u001B[31mKhông tìm thấy nhân viên nào trong khoảng tuổi từ " + minAge + " đến " + maxAge + "\u001B[0m");
            } else {
                System.out.println("\u001B[36m\n== Kết quả tìm kiếm ==\u001B[0m");
                for (EmployeeModel emp : results) {
                    System.out.println(emp);
                }
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mĐã xảy ra lỗi khi tìm kiếm nhân viên theo tuổi: " + e.getMessage() + "\u001B[0m");
        }
    }


    private static void sortEmployees(Scanner scanner, EmployeeServiceImp employeeService) {
        System.out.println("\u001B[36m\n== Sắp xếp nhân viên ==\u001B[0m");
        System.out.println("1. Tên tăng dần (A-Z)");
        System.out.println("2. Lương giảm dần");
        System.out.println("3. Quay lại");
        System.out.print("Lựa chọn: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            List<EmployeeModel> sortedList = null;
            String sortType = "";

            switch (choice) {
                case 1 -> {
                    sortedList = employeeService.sortByName(true);
                    sortType = "tên tăng dần ";
                }
                case 2 -> {
                    sortedList = employeeService.sortBySalary(false);
                    sortType = "lương giảm dần";
                }
                case 3 -> {
                    return;
                }
                default -> {
                    System.out.println("\u001B[31mLựa chọn không hợp lệ! Vui lòng chọn lại.\u001B[0m");
                    return;
                }
            }

            // Phân trang
            if (sortedList != null && !sortedList.isEmpty()) {
                int pageSize = 5; // Số bản ghi trên mỗi trang
                int totalItems = sortedList.size(); // Tổng số nhân viên
                int totalPages = (int) Math.ceil(totalItems / (double) pageSize); // Tính tổng số trang
                int currentPage = 1; // Mặc định bắt đầu từ trang 1

                while (true) {
                    // Hiển thị danh sách nhân viên cho trang hiện tại
                    System.out.println("\u001B[36m\n== Danh sách nhân viên sắp xếp theo " + sortType + " (Trang " + currentPage + " / " + totalPages + ") ==\u001B[0m");

                    int startIndex = (currentPage - 1) * pageSize;
                    int endIndex = Math.min(startIndex + pageSize, totalItems);

                    for (int i = startIndex; i < endIndex; i++) {
                        System.out.println(sortedList.get(i));
                    }

                    System.out.println("\nChọn một trong các lựa chọn sau:");
                    System.out.println("1. Trang tiếp theo");
                    System.out.println("2. Trang trước");
                    System.out.println("3. Chọn trang cụ thể");
                    System.out.println("4. Quay lại");
                    String input = scanner.nextLine();

                    switch (input) {
                        case "1":
                            if (currentPage < totalPages) {
                                currentPage++;
                            } else {
                                System.out.println("\u001B[31mĐây là trang cuối.\u001B[0m");
                            }
                            break;
                        case "2":
                            if (currentPage > 1) {
                                currentPage--;
                            } else {
                                System.out.println("\u001B[31mĐây là trang đầu.\u001B[0m");
                            }
                            break;
                        case "3":
                            System.out.print("Nhập số trang bạn muốn đến (1-" + totalPages + "): ");
                            try {
                                int page = Integer.parseInt(scanner.nextLine());
                                if (page >= 1 && page <= totalPages) {
                                    currentPage = page;
                                } else {
                                    System.out.println("\u001B[31mSố trang không hợp lệ.\u001B[0m");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("\u001B[31mVui lòng nhập một số hợp lệ!\u001B[0m");
                            }
                            break;
                        case "4":
                            return;
                        default:
                            System.out.println("\u001B[31mLựa chọn không hợp lệ!\u001B[0m");
                            break;
                    }
                }
            } else {
                System.out.println("\u001B[31mKhông có nhân viên nào.\u001B[0m");
            }
        } catch (NumberFormatException e) {
            System.out.println("\u001B[31mVui lòng nhập số hợp lệ!\u001B[0m");
        } catch (Exception e) {
            System.out.println("\u001B[31mĐã xảy ra lỗi khi sắp xếp nhân viên: " + e.getMessage() + "\u001B[0m");
        }
    }
}