package business.dao.login;

import business.config.ConnectionDB;
import business.model.user.UserModel;

import java.sql.CallableStatement;
import java.sql.Connection;

public class LoginDAOImp implements LoginDAO{

    @Override
    public boolean checkLogin(UserModel user) {
        String call = "{CALL check_login(?, ?, ?)}";

        try (Connection conn = ConnectionDB.openConnection();
             CallableStatement stmt = conn.prepareCall(call)) {

            // Truyền tham số IN
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            // Đăng ký tham số OUT
            stmt.registerOutParameter(3, java.sql.Types.BOOLEAN);

            // Thực thi stored procedure
            stmt.execute();

            // Lấy giá trị từ biến OUT
            return stmt.getBoolean(3);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
