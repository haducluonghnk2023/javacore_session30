package presentation;

import business.model.department.DepartmentModel;
import business.service.department.DepartmentServiceImp;
import business.util.PageInfo;

import java.util.List;
import java.util.Scanner;

public class DepartmentUI {
    public static void displayDepartmentMenu(Scanner scanner) {
        DepartmentServiceImp departmentService = new DepartmentServiceImp();
        boolean continueProgram = true;

        do {
            System.out.println("\n========== MENU PHÒNG BAN ==========");
            System.out.println("1. Danh sách phòng ban có phân trang");
            System.out.println("2. Thêm mới phòng ban");
            System.out.println("3. Cập nhật phòng ban");
            System.out.println("4. Xóa phòng ban");
            System.out.println("5. Tìm kiếm phòng ban theo tên");
            System.out.println("6. Thoát");
            System.out.print("Chọn chức năng: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> displayDepartmentList(scanner, departmentService);
                case 2 -> addDepartment(scanner, departmentService);
                case 3 -> updateDepartment(scanner, departmentService);
                case 4 -> deleteDepartment(scanner, departmentService);
                case 5 -> searchDepartmentByName(scanner, departmentService);
                case 6 -> {
                    continueProgram = false;
                    System.out.println("Thoát chương trình...");
                }
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (continueProgram);
    }

    private static void displayDepartmentList(Scanner scanner, DepartmentServiceImp departmentService) {
        int currentPage = 1;
        int pageSize = 5;
        boolean continueList = true;

        while (continueList) {
            try {
                PageInfo<DepartmentModel> pageInfo = departmentService.getPageData(currentPage, pageSize);
                List<DepartmentModel> departments = pageInfo.getRecords();

                System.out.println("\u001B[36m\n--- Danh sách phòng ban (Trang " + pageInfo.getCurrentPage() + "/" + pageInfo.getTotalPages() + ") --- Tổng " + pageInfo.getTotalRecords() + " phòng ban\u001B[0m");
                if (departments.isEmpty()) {
                    System.out.println("\u001B[31mKhông có phòng ban nào ở trang này.\u001B[0m");
                } else {
                    for (DepartmentModel dpt : departments) {
                        System.out.println(dpt);
                    }
                }

                System.out.println("\u001B[36m\n== Tùy chọn điều hướng ==\u001B[0m");
                System.out.println("1. Trang tiếp");
                System.out.println("2. Trang trước");
                System.out.println("3. Đến trang cụ thể");
                System.out.println("4. Quay lại menu chính");
                System.out.print("Lựa chọn: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

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
                        int targetPage = scanner.nextInt();
                        scanner.nextLine();
                        if (targetPage >= 1 && targetPage <= pageInfo.getTotalPages()) {
                            currentPage = targetPage;
                        } else {
                            System.out.println("\u001B[31mSố trang không hợp lệ.\u001B[0m");
                        }
                    }
                    case 4 -> continueList = false;
                    default -> System.out.println("\u001B[31mLựa chọn không hợp lệ.\u001B[0m");
                }

            } catch (Exception e) {
                System.out.println("\u001B[31mĐã xảy ra lỗi: " + e.getMessage() + "\u001B[0m");
                scanner.nextLine(); // clear buffer
                continueList = false;
            }
        }
    }

    private static void addDepartment(Scanner scanner, DepartmentServiceImp departmentService) {
        DepartmentModel newDpt = new DepartmentModel();
        newDpt.inputData(scanner);

        boolean isAdded = departmentService.add(newDpt);
        if (isAdded) {
            System.out.println("\u001B[32mThêm phòng ban thành công.\u001B[0m");
        } else {
            System.out.println("\u001B[31mThêm phòng ban thất bại.\u001B[0m");
        }
    }

    private static void updateDepartment(Scanner scanner, DepartmentServiceImp departmentService) {
        System.out.print("Nhập ID phòng ban cần cập nhật: ");
        int departmentId = Integer.parseInt(scanner.nextLine());

        DepartmentModel existingDpt = departmentService.findById(departmentId);
        if (existingDpt == null) {
            System.out.println("\u001B[31mKhông tìm thấy phòng ban với ID: " + departmentId + "\u001B[0m");
            return;
        }

        DepartmentModel updatedDpt = new DepartmentModel();
        updatedDpt.setDepartmentId(departmentId);
        updatedDpt.setDepartmentName(existingDpt.getDepartmentName());
        updatedDpt.setDepartmentDescription(existingDpt.getDepartmentDescription());
        updatedDpt.setStatus(existingDpt.isStatus());

        System.out.println("\u001B[36m\n== Chọn thông tin muốn cập nhật ==\u001B[0m");
        System.out.println("1. Tên phòng ban");
        System.out.println("2. Mô tả phòng ban");
        System.out.println("3. Trạng thái");
        System.out.println("4. Cập nhật tất cả");
        System.out.print("Lựa chọn của bạn: ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> {
                System.out.print("Nhập tên phòng ban mới: ");
                updatedDpt.setDepartmentName(scanner.nextLine());
            }
            case 2 -> {
                System.out.print("Nhập mô tả mới: ");
                updatedDpt.setDepartmentDescription(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Trạng thái (true: hoạt động, false: ngưng): ");
                updatedDpt.setStatus(scanner.nextBoolean());
                scanner.nextLine();
            }
            case 4 -> updatedDpt.inputData(scanner);
            default -> {
                System.out.println("\u001B[31mLựa chọn không hợp lệ!\u001B[0m");
                return;
            }
        }

        boolean isUpdated = departmentService.update(updatedDpt);
        if (isUpdated) {
            System.out.println("\u001B[32mCập nhật phòng ban thành công.\u001B[0m");
        } else {
            System.out.println("\u001B[31mCập nhật phòng ban thất bại.\u001B[0m");
        }
    }

    private static void deleteDepartment(Scanner scanner, DepartmentServiceImp departmentService) {
        System.out.print("Nhập ID phòng ban cần xóa: ");
        String departmentId = scanner.nextLine();
        String message = departmentService.delete(departmentId);
        if (message.contains("successfully")) {
            System.out.println("\u001B[32m" + message + "\u001B[0m");
        } else if (message.contains("Cannot delete")) {
            System.out.println("\u001B[31m" + message + "\u001B[0m");
        } else if (message.contains("does not exist")) {
            System.out.println("\u001B[33m" + message + "\u001B[0m");
        } else {
            System.out.println(message);
        }
    }

    private static void searchDepartmentByName(Scanner scanner, DepartmentServiceImp departmentService) {
        System.out.print("Nhập tên phòng ban cần tìm kiếm: ");
        String keyword = scanner.nextLine();

        List<DepartmentModel> results = departmentService.searchByName(keyword);
        if (results.isEmpty()) {
            System.out.println("\u001B[31mKhông tìm thấy phòng ban nào với tên: " + keyword + "\u001B[0m");
        } else {
            System.out.println("\u001B[36m\n== Kết quả tìm kiếm ==\u001B[0m");
            for (DepartmentModel dpt : results) {
                System.out.println(dpt);
            }
        }
    }
}
