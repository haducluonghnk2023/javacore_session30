package business.service.employee;

import business.model.employee.EmployeeModel;
import business.service.AppService;

import java.util.List;

public interface EmployeeService extends AppService {
    List<EmployeeModel> searchByAgeRange(int minAge, int maxAge); // Tìm theo khoảng tuổi

    List<EmployeeModel> sortByName(boolean asc);
    List<EmployeeModel> sortBySalary(boolean desc);


    EmployeeModel findById(String id);
}
