package business.model.department;

import validate.validateDepartment.ValidatorDepartment;

import java.util.Scanner;

public class DepartmentModel implements DepartmentInterface {
    private int departmentId;
    private String departmentName;
    private String departmentDescription;
    private boolean status;

    // Constructors
    public DepartmentModel() {
    }

    public DepartmentModel(String departmentName, String departmentDescription, boolean status) {
        this.departmentName = departmentName;
        this.departmentDescription = departmentDescription;
        this.status = status;
    }

    public DepartmentModel(int departmentId, String departmentName, String departmentDescription, boolean status) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentDescription = departmentDescription;
        this.status = status;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentDescription() {
        return departmentDescription;
    }

    public void setDepartmentDescription(String departmentDescription) {
        this.departmentDescription = departmentDescription;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    // Input dữ liệu từ người dùng
    @Override
    public void inputData(Scanner scanner) {
        while (true) {
            System.out.print("Nhập tên phòng ban (10-100 ký tự): ");
            String name = scanner.nextLine();
            if (ValidatorDepartment.isValidDepartmentName(name, null)) {
                this.departmentName = name;
                break;
            } else {
                System.out.println("Tên không hợp lệ! Vui lòng nhập lại.");
            }
        }

        while (true) {
            System.out.print("Nhập mô tả phòng ban (tối đa 255 ký tự): ");
            String desc = scanner.nextLine();
            if (ValidatorDepartment.isValidDepartmentDescription(desc)) {
                this.departmentDescription = desc;
                break;
            } else {
                System.out.println("Mô tả quá dài hoặc không hợp lệ.");
            }
        }

        System.out.print("Trạng thái phòng ban (true = hoạt động / false = không hoạt động): ");
        this.status = scanner.nextBoolean();
        scanner.nextLine();
    }

    @Override
    public String toString() {
        return String.format("| %-3d | %-20s | %-30s | %-15s |",
                departmentId,
                departmentName,
                departmentDescription,
                status ? "Hoạt động" : "Không hoạt động");
    }

}
