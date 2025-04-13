package business.service.login;

import business.model.user.UserModel;

public interface LoginService {
    boolean checkLogin(UserModel user);
}
