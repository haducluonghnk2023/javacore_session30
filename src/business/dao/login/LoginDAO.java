package business.dao.login;

import business.model.user.UserModel;

public interface LoginDAO {
    boolean checkLogin(UserModel user);
}
