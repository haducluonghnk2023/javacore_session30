package business.dao.employee;

import business.config.ConnectionDB;
import business.model.employee.EmployeeModel;
import business.model.employee.Sex;
import business.model.employee.Status;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class EmployeeDAOImp implements EmployeeDAO {

    // Lấy dữ liệu nhân viên theo trang
    @Override
    public List<EmployeeModel> getPageData(int page, int size) {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = "{CALL GetEmployeesByPage(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, page);
            callSt.setInt(2, size);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách nhân viên phân trang: " + e.getMessage());
        }

        return employees;
    }

    // Thêm nhân viên nếu phòng ban còn hoạt động
    @Override
    public boolean add(EmployeeModel obj) {
        String sql = "{CALL AddEmployeeIfDepartmentActive(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, obj.getEmployeeId());
            callSt.setString(2, obj.getEmployeeName());
            callSt.setDate(3, Date.valueOf(obj.getEmployeeBirthday()));
            callSt.setString(4, obj.getEmployeePhone());
            callSt.setString(5, obj.getEmployeeEmail());
            callSt.setString(6, obj.getEmployeeAddress());
            callSt.setDouble(7, obj.getEmployeeSalaryLevel());
            callSt.setString(8, obj.getEmployeeSex().name());
            callSt.setString(9, obj.getEmployeeStatus().name());
            callSt.setInt(10, obj.getDepartmentId());
            callSt.setDouble(11, obj.getEmployeeSalary());
            callSt.registerOutParameter(12, Types.VARCHAR);

            callSt.execute();

            String message = callSt.getString(12); // LẤY GIÁ TRỊ OUT
            System.out.println("Thông báo thêm nhân viên: " + message);

            return message != null && message.toLowerCase().contains("thành công");

        } catch (SQLException e) {
            System.err.println("Lỗi thêm nhân viên: " + e.getMessage());
            return false;
        }
    }


    // Cập nhật thông tin nhân viên
    @Override
    public boolean update(EmployeeModel updatedEmp) {
        String sql = "{CALL UpdateEmployee(?, ?, ?, ?, ?, ?, ?, ?, ?,?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, updatedEmp.getEmployeeId());
            callSt.setString(2, updatedEmp.getEmployeeName());
            callSt.setDate(3, java.sql.Date.valueOf(updatedEmp.getEmployeeBirthday()));
            callSt.setString(4, updatedEmp.getEmployeePhone());
            callSt.setString(5, updatedEmp.getEmployeeEmail());
            callSt.setString(6, updatedEmp.getEmployeeAddress());
            callSt.setDouble(7, updatedEmp.getEmployeeSalary());
            callSt.setDouble(8, updatedEmp.getEmployeeSalaryLevel());
            callSt.setString(9, updatedEmp.getEmployeeSex().toString());
            callSt.setString(10, updatedEmp.getEmployeeStatus().toString());

            int rowsUpdated = callSt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật nhân viên: " + e.getMessage());
        }
        return false;
    }


    // Xóa nhân viên có kiểm tra ràng buộc
    @Override
    public String delete(String id) {
        String sql = "{CALL SoftDeleteEmployee(?)}";  // Chỉ cần 1 tham số

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, id);  // Truyền tham số id
            callSt.execute();
            return "Nhân viên đã bị xóa thành công";  // Không cần lấy giá trị từ tham số thứ 3

        } catch (SQLException e) {
            return "Lỗi khi xóa nhân viên: " + e.getMessage();
        }
    }


    // Sắp xếp nhân viên theo lương giảm dần
    @Override
    public List<EmployeeModel> sortBySalaryDesc() {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = "{CALL SortEmployeesBySalaryDesc()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi sắp xếp nhân viên theo lương: " + e.getMessage());
        }

        return employees;
    }

    // Sắp xếp nhân viên theo tên tăng dần
    @Override
    public List<EmployeeModel> sortByNameAsc() {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = "{CALL SortEmployeesByNameAsc()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi sắp xếp nhân viên theo tên: " + e.getMessage());
        }

        return employees;
    }

    // Tìm kiếm nhân viên theo tên
    @Override
    public List<EmployeeModel> searchByName(String name) {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = "{CALL SearchEmployeeByName(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, "%" + name + "%");

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm nhân viên theo tên: " + e.getMessage());
        }

        return employees;
    }

    // Tìm kiếm nhân viên theo khoảng tuổi
    @Override
    public List<EmployeeModel> searchByAgeRange(int minAge, int maxAge) {
        List<EmployeeModel> employees = new ArrayList<>();
        String sql = "{CALL SearchEmployeeByAgeRange(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, minAge);
            callSt.setInt(2, maxAge);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm theo tuổi: " + e.getMessage());
        }

        return employees;
    }

    private EmployeeModel mapResultSetToEmployee(ResultSet rs) throws SQLException {
        String employeeId = rs.getString("employee_id");
        String employeeName = rs.getString("employee_name");
        LocalDate employeeBirthday = rs.getDate("employee_birthday") != null
                ? rs.getDate("employee_birthday").toLocalDate()
                : null;
        String employeePhone = rs.getString("employee_phone");
        String employeeEmail = rs.getString("employee_email");
        String employeeAddress = rs.getString("employee_address");
        double employeeSalaryLevel = rs.getDouble("employee_salary_level");
        Sex employeeSex = Sex.valueOf(rs.getString("employee_sex"));
        Status employeeStatus = Status.valueOf(rs.getString("employee_status"));
        int departmentId = rs.getInt("department_id");
        double employeeSalary = rs.getDouble("employee_salary");

        return new EmployeeModel(
                employeeId,
                employeeName,
                employeeEmail,
                employeePhone,
                employeeSex,
                employeeSalaryLevel,
                employeeSalary,
                employeeBirthday,
                employeeAddress,
                employeeStatus,
                departmentId
        );
    }

    @Override
    public int getTotalEmployees() {
        int total = 0;
        String sql = "{CALL GetTotalEmployees()}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql);
             ResultSet rs = callSt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi gọi GetTotalDepartments: " + e.getMessage());
        }

        return total;
    }

    @Override
    public EmployeeModel findById(String id) {
        String sql = "{CALL FindEmployeeById(?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, id);

            try (ResultSet rs = callSt.executeQuery()) {
                if (rs.next()) {
                    EmployeeModel emp = new EmployeeModel();
                    emp.setEmployeeId(rs.getString("employee_id"));
                    emp.setEmployeeName(rs.getString("employee_name"));
                    emp.setEmployeeEmail(rs.getString("employee_email"));
                    emp.setEmployeePhone(rs.getString("employee_phone"));
                    emp.setEmployeeAddress(rs.getString("employee_address"));

                    // Giới tính
                    String genderStr = rs.getString("employee_sex");
                    if (genderStr != null) {
                        switch (genderStr.toLowerCase()) {
                            case "male" -> emp.setEmployeeSex(Sex.MALE);
                            case "female" -> emp.setEmployeeSex(Sex.FEMALE);
                            default -> emp.setEmployeeSex(Sex.OTHER);
                        }
                    } else {
                        emp.setEmployeeSex(Sex.OTHER);
                    }

                    // Ngày sinh
                    emp.setEmployeeBirthday(rs.getDate("employee_birthday") != null
                            ? rs.getDate("employee_birthday").toLocalDate()
                            : null);

                    // LƯƠNG (bị thiếu)
                    emp.setEmployeeSalary(rs.getDouble("employee_salary"));

                    // Cấp bậclương
                    emp.setEmployeeSalaryLevel(rs.getDouble("employee_salary_level"));

                    // Trạng thái
                    String statusStr = rs.getString("employee_status");
                    if (statusStr != null) {
                        try {
                            emp.setEmployeeStatus(Status.valueOf(statusStr.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            emp.setEmployeeStatus(Status.INACTIVE);
                        }
                    } else {
                        emp.setEmployeeStatus(Status.INACTIVE);
                    }

                    emp.setDepartmentId(rs.getInt("department_id"));
                    return emp;
                } else {
                    throw new NoSuchElementException("Không tìm thấy nhân viên với ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm nhân viên theo ID: " + e.getMessage());
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
