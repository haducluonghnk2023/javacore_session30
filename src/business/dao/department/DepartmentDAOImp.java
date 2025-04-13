package business.dao.department;

import business.config.ConnectionDB;
import business.model.department.DepartmentModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOImp implements DepartmentDAO {

    @Override
    public List<DepartmentModel> getPageData(int page, int size) {
        List<DepartmentModel> departments = new ArrayList<>();

        // Gọi đúng tên procedure mới
        String sql = "{CALL GetDepartmentPagination(?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, page);
            callSt.setInt(2, size);

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    DepartmentModel dept = new DepartmentModel();
                    dept.setDepartmentId(rs.getInt("department_id"));
                    dept.setDepartmentName(rs.getString("department_name"));
                    dept.setDepartmentDescription(rs.getString("department_description"));
                    dept.setStatus(rs.getBoolean("status"));
                    departments.add(dept);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving paginated departments: " + e.getMessage());
        }

        return departments;
    }

    @Override
    public boolean add(DepartmentModel obj) {
        String sql = "{CALL AddDepartment(?, ?, ?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, obj.getDepartmentName());
            callSt.setString(2, obj.getDepartmentDescription() != null ? obj.getDepartmentDescription() : "");
            callSt.setBoolean(3, obj.isStatus());

            return callSt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding department: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(DepartmentModel obj) {
        if (obj.getDepartmentName() == null || obj.getDepartmentName().trim().isEmpty()) {
            System.err.println("Tên phòng ban không được để trống.");
            return false;
        }

        String sql = "{CALL UpdateDepartment(?, ?, ?, ?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, obj.getDepartmentId());
            callSt.setString(2, obj.getDepartmentName());
            callSt.setString(3, obj.getDepartmentDescription() != null ? obj.getDepartmentDescription() : "");
            callSt.setBoolean(4, obj.isStatus());

            return callSt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating department: " + e.getMessage());
            return false;
        }
    }


    @Override
    public String delete(String id) {
        String sql = "{CALL SafeDeleteDepartment(?, ?, ?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            // Set IN parameter
            callSt.setString(1, id);

            // Register OUT parameters
            callSt.registerOutParameter(2, java.sql.Types.BOOLEAN);
            callSt.registerOutParameter(3, java.sql.Types.VARCHAR);

            // Execute procedure
            callSt.execute();

            // Get OUT value
            String message = callSt.getString(3);

            System.out.println(message);
            return message;

        } catch (SQLException e) {
            return "Lỗi khi xóa phòng ban: " + e.getMessage();
        }
    }


    @Override
    public List<DepartmentModel> searchByName(String name) {
        List<DepartmentModel> departments = new ArrayList<>();
        String sql = "{CALL SearchDepartmentByName(?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setString(1, "%" + name + "%");

            try (ResultSet rs = callSt.executeQuery()) {
                while (rs.next()) {
                    DepartmentModel dept = new DepartmentModel();
                    dept.setDepartmentId(rs.getInt("department_id"));
                    dept.setDepartmentName(rs.getString("department_name"));
                    dept.setDepartmentDescription(rs.getString("department_description"));
                    dept.setStatus(rs.getBoolean("status"));
                    departments.add(dept);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching departments: " + e.getMessage());
        }

        return departments;
    }
    @Override
    public int getTotalDepartments() {
        int total = 0;
        String sql = "{CALL GetTotalDepartments()}";

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
    public DepartmentModel findById(int id) {
        String sql = "{CALL FindDepartmentById(?)}";
        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement callSt = conn.prepareCall(sql)) {

            callSt.setInt(1, id);

            try (ResultSet rs = callSt.executeQuery()) {
                if (rs.next()) {
                    DepartmentModel dept = new DepartmentModel();
                    dept.setDepartmentId(rs.getInt("department_id"));
                    dept.setDepartmentName(rs.getString("department_name"));
                    dept.setDepartmentDescription(rs.getString("department_description"));
                    dept.setStatus(rs.getBoolean("status"));
                    return dept;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm phòng ban theo ID: " + e.getMessage());
        }
        return null;
    }
}
