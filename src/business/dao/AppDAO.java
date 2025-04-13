package business.dao;

import java.util.List;

public interface AppDAO<T> {
    List<T> getPageData(int page, int size);

    // Thêm mới
    boolean add(T obj);

    // Cập nhật
    boolean update(T obj);

    // Xóa
    String delete(String id);

    // Tìm kiếm theo tên
    List<T> searchByName(String name);
}
