package business.service.login;

import business.dao.login.LoginDAO;
import business.dao.login.LoginDAOImp;
import business.model.user.UserModel;

public class LoginServiceImp implements LoginService {
    private LoginDAO loginDAO ;
    public LoginServiceImp() {
        this.loginDAO = new LoginDAOImp();
    }
    @Override
    public boolean checkLogin(UserModel user) {
        return loginDAO.checkLogin(user);
    }
}
