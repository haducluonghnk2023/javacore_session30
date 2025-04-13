package business.model.employee;

import validate.validateEmployee.ValidatorEmployee;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class EmployeeModel implements EmployeeModelInterface{
    private String employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeePhone;
    private Sex employeeSex;
    private double employeeSalaryLevel;
    private double employeeSalary;
    private LocalDate employeeBirthday;
    private String employeeAddress;
    private Status employeeStatus;
    private int departmentId;

    public EmployeeModel() {
    }


    public EmployeeModel(String employeeId, String employeeName, String employeeEmail, String employeePhone, Sex employeeSex, double employeeSalaryLevel, double employeeSalary, LocalDate employeeBirthday, String employeeAddress, Status employeeStatus, int departmentId) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.employeePhone = employeePhone;
        this.employeeSex = employeeSex;
        this.employeeSalaryLevel = employeeSalaryLevel;
        this.employeeSalary = employeeSalary;
        this.employeeBirthday = employeeBirthday;
        this.employeeAddress = employeeAddress;
        this.employeeStatus = employeeStatus;
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(double employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public Sex getEmployeeSex() {
        return employeeSex;
    }

    public void setEmployeeSex(Sex employeeSex) {
        this.employeeSex = employeeSex;
    }

    public double getEmployeeSalaryLevel() {
        return employeeSalaryLevel;
    }

    public void setEmployeeSalaryLevel(double employeeSalaryLevel) {
        this.employeeSalaryLevel = employeeSalaryLevel;
    }

    public LocalDate getEmployeeBirthday() {
        return employeeBirthday;
    }

    public void setEmployeeBirthday(LocalDate employeeBirthday) {
        this.employeeBirthday = employeeBirthday;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public Status getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Status employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    @Override
    public void inputData(Scanner scanner) {
        // Shared set for existing employee IDs
        Set<String> existingEmployeeIds = new HashSet<>();

        // Input and validate employee ID
        do {
            System.out.print("Enter employee ID (5 characters, starts with 'E', unique): ");
            String id = scanner.nextLine();
            if (ValidatorEmployee.isValidEmployeeId(id, existingEmployeeIds)) {
                this.employeeId = id;
                existingEmployeeIds.add(id);
                break;
            } else {
                System.out.println("Invalid employee ID. Ensure it starts with 'E', is 5 characters long, and unique.");
            }
        } while (true);

        // Input and validate employee name
        do {
            System.out.print("Enter employee name (15-150 characters): ");
            String name = scanner.nextLine();
            if (ValidatorEmployee.isValidEmployeeName(name)) {
                this.employeeName = name;
                break;
            } else {
                System.out.println("Invalid employee name. Ensure it is 15-150 characters long.");
            }
        } while (true);

        // Input and validate employee email
        do {
            System.out.print("Enter employee email: ");
            String email = scanner.nextLine();
            if (ValidatorEmployee.isValidEmail(email)) {
                this.employeeEmail = email;
                break;
            } else {
                System.out.println("Invalid email format.");
            }
        } while (true);

        // Input and validate employee phone
        do {
            System.out.print("Enter employee phone (VN format): ");
            String phone = scanner.nextLine();
            if (ValidatorEmployee.isValidPhoneNumber(phone)) {
                this.employeePhone = phone;
                break;
            } else {
                System.out.println("Invalid phone number format.");
            }
        } while (true);

        // Input and validate employee sex
        do {
            System.out.print("Enter employee sex (MALE, FEMALE, OTHER): ");
            String sexInput = scanner.nextLine().toUpperCase();
            if (Sex.isValid(sexInput)) {
                this.employeeSex = Sex.valueOf(sexInput);
                break;
            } else {
                System.out.println("Invalid sex. Choose from MALE, FEMALE, OTHER.");
            }
        } while (true);

        // Input and validate salary level
        do {
            System.out.print("Enter salary level (positive integer): ");
            try {
                int salaryLevel = scanner.nextInt();
                scanner.nextLine();
                if (ValidatorEmployee.isPositiveInteger(salaryLevel)) {
                    this.employeeSalaryLevel = salaryLevel;
                    break;
                } else {
                    System.out.println("Invalid salary level. Must be a positive integer.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.nextLine(); // Clear invalid input
            }
        } while (true);

        // Input and validate salary
        do {
            System.out.print("Enter salary (positive number): ");
            try {
                double salary = Double.parseDouble(scanner.nextLine());
                if (ValidatorEmployee.isPositiveDouble(salary)) {
                    this.employeeSalary = salary;
                    break;
                } else {
                    System.out.println("Invalid salary. Must be a positive number.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a positive number.");
                scanner.nextLine();
            }
        } while (true);

        scanner.nextLine();

        // Input and validate birthday
        do {
            System.out.print("Enter birthday (dd/MM/yyyy): ");
            String birthday = scanner.nextLine();
            if (ValidatorEmployee.isValidDate(birthday, "dd/MM/yyyy")) {
                this.employeeBirthday = ValidatorEmployee.parseDate(birthday, "dd/MM/yyyy");
                break;
            } else {
                System.out.println("Invalid date format. Use dd/MM/yyyy.");
            }
        } while (true);

        // Input and validate address
        do {
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            if (ValidatorEmployee.isNotNullOrEmpty(address)) {
                this.employeeAddress = address;
                break;
            } else {
                System.out.println("Address cannot be empty.");
            }
        } while (true);

        // Input and validate employee status
        do {
            System.out.print("Enter employee status (ACTIVE, INACTIVE, ONLEAVE, POLICYLEAVE): ");
            String statusInput = scanner.nextLine().toUpperCase();
            if (Status.isValid(statusInput)) {
                this.employeeStatus = Status.valueOf(statusInput);
                break;
            } else {
                System.out.println("Invalid status. Choose from ACTIVE, INACTIVE, ONLEAVE, POLICYLEAVE.");
            }
        } while (true);

        do {
            System.out.print("Enter department ID: ");
            try {
                String input = scanner.nextLine();
                int departmentId = Integer.parseInt(input);
                if (departmentId > 0) {
                    this.departmentId = departmentId;
                    break;
                } else {
                    System.out.println("Department ID must be a positive integer.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid department ID.");
            }
        } while (true);
    }


    @Override
    public String toString() {
        return String.format("| %-15s | %-20s | %-30s | %-15s | %-10s | %-15.2f | %-15.2f | %-12s | %-30s | %-20s | %-12d |",
                employeeId,
                employeeName,
                employeeEmail,
                employeePhone,
                employeeSex != null ? employeeSex.name() : "Không xác định",
                employeeSalaryLevel,
                employeeSalary,
                employeeBirthday != null ? employeeBirthday.toString() : "Không xác định",
                employeeAddress,
                employeeStatus != null ? employeeStatus.name() : "Không xác định",
                departmentId);
    }
}
