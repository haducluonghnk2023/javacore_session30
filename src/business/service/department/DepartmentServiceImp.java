package business.service.department;

import business.dao.department.DepartmentDAOImp;
import business.model.department.DepartmentModel;
import business.util.PageInfo;

import java.util.List;

public class DepartmentServiceImp implements DepartmentService {
    private DepartmentDAOImp departmentDAO;

    public DepartmentServiceImp() {
        this.departmentDAO = new DepartmentDAOImp();
    }

    @Override
    public PageInfo<DepartmentModel> getPageData(int page, int size) {
        List<DepartmentModel> departments = departmentDAO.getPageData(page, size);
        int totalRecords = departmentDAO.getTotalDepartments();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        PageInfo<DepartmentModel> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);
        pageInfo.setPageSize(size);
        pageInfo.setTotalRecords(totalRecords);
        pageInfo.setTotalPages(totalPages);
        pageInfo.setRecords(departments);

        return pageInfo;
    }


    @Override
    public boolean add(Object obj) {
        if (obj instanceof DepartmentModel) {
            return departmentDAO.add((DepartmentModel) obj);
        }
        return false;
    }

    @Override
    public boolean update(Object obj) {
        if (obj instanceof DepartmentModel) {
            return departmentDAO.update((DepartmentModel) obj);
        }
        return false;
    }

    @Override
    public String delete(String id) {
        return departmentDAO.delete(id);
    }

    @Override
    public List<DepartmentModel> searchByName(String name) {
        return departmentDAO.searchByName(name);
    }

    @Override
    public DepartmentModel findById(int id) {
        return departmentDAO.findById(id);
    }
}
