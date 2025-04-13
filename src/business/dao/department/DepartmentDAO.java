package business.dao.department;

import business.dao.AppDAO;
import business.model.department.DepartmentModel;


public interface DepartmentDAO extends AppDAO<DepartmentModel> {
    int getTotalDepartments();
    DepartmentModel findById(int id);
}
