package business.service.employee;

import business.dao.employee.EmployeeDAO;
import business.dao.employee.EmployeeDAOImp;
import business.model.employee.EmployeeModel;
import business.util.PageInfo;

import java.util.List;

public class EmployeeServiceImp implements EmployeeService {
    private EmployeeDAO employeeDAO;

    public EmployeeServiceImp() {
        this.employeeDAO = new EmployeeDAOImp();
    }

    @Override
    public List<EmployeeModel> searchByAgeRange(int minAge, int maxAge) {
        return employeeDAO.searchByAgeRange(minAge, maxAge);
    }

    @Override
    public List<EmployeeModel> sortByName(boolean asc) {
        List<EmployeeModel> list = employeeDAO.sortByNameAsc();
        return list;
    }

    @Override
    public List<EmployeeModel> sortBySalary(boolean desc) {
        List<EmployeeModel> list = employeeDAO.sortBySalaryDesc();
        return list;
    }



    @Override
    public EmployeeModel findById(String id) {
        return employeeDAO.findById(id);
    }

    @Override
    public PageInfo<EmployeeModel> getPageData(int page, int size) {
        // Lấy dữ liệu nhân viên cho trang hiện tại
        List<EmployeeModel> employees = employeeDAO.getPageData(page, size);

        // Lấy tổng số nhân viên trong cơ sở dữ liệu
        int totalRecords = employeeDAO.getTotalEmployees();

        // Tính toán tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Tạo đối tượng PageInfo để chứa thông tin phân trang
        PageInfo<EmployeeModel> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(page);           // Trang hiện tại
        pageInfo.setPageSize(size);              // Kích thước mỗi trang
        pageInfo.setTotalRecords(totalRecords);  // Tổng số bản ghi
        pageInfo.setTotalPages(totalPages);      // Tổng số trang
        pageInfo.setRecords(employees);          // Dữ liệu nhân viên trong trang hiện tại

        return pageInfo;
    }


    @Override
    public boolean add(Object obj) {
        return employeeDAO.add((EmployeeModel)obj);
    }

    @Override
    public boolean update(Object obj) {
        return employeeDAO.update((EmployeeModel)obj);
    }

    @Override
    public String delete(String id) {
        return employeeDAO.delete(id);
    }

    @Override
    public List searchByName(String name) {
        return employeeDAO.searchByName(name);
    }
}
