package business.service.department;

import business.model.department.DepartmentModel;
import business.service.AppService;

public interface DepartmentService extends AppService {
    DepartmentModel findById(int id);
}
