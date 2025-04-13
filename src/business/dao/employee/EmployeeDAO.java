package business.dao.employee;

import business.dao.AppDAO;
import business.model.employee.EmployeeModel;

import java.util.List;

public interface EmployeeDAO extends AppDAO<EmployeeModel> {
    List<EmployeeModel> searchByAgeRange(int minAge, int maxAge); // Tìm theo khoảng tuổi
    List<EmployeeModel> sortBySalaryDesc();  // Lương giảm dần
    List<EmployeeModel> sortByNameAsc();     // Tên tăng dần
    int getTotalEmployees();
    EmployeeModel findById(String id);
}