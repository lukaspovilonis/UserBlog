package lt.userblog.services;


import lombok.Getter;
import lombok.Setter;
import lt.userblog.entities.User;
import lt.userblog.persistence.UserDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject
    private UserDAO userDAO;

    @Transactional
    public boolean ifExistName(String name){
        return userDAO.ifExistName(name);
    }

    @Transactional
    public User update(User user ){
        return userDAO.update(user);
    }

    @Transactional
    public User findOne(Integer id){
       return userDAO.findOne(id);
    }

    @Transactional
    public User findOne(String name){
        return userDAO.findOne(name);
    }
}
