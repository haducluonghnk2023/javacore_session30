package business.service;


import business.util.PageInfo;

import java.util.List;

public interface AppService<T> {
    PageInfo<T> getPageData(int page, int size);  // lấy danh sách phân trang

    // Thêm mới
    boolean add(T obj);

    // Cập nhật
    boolean update(T obj);

    // Xóa
    String delete(String id); // hoặc boolean deleteById(int id);

    // Tìm kiếm theo tên
    List<T> searchByName(String name);
}
