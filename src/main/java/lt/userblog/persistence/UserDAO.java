package lt.userblog.persistence;

import lt.userblog.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class UserDAO {

    @Inject
    private EntityManager em;

    public User findOne(Integer id){
        return em.find(User.class, id);
    }

    public User findOne(String name){
        return em.createQuery("SELECT u from User u where u.user_name =?1", User.class).setParameter(1, name).getSingleResult();
    }

    public User update(User user){
        return em.merge(user);
    }

    public boolean ifExistName(String name) {
        return em.createQuery("SELECT count(u.user_name) from User u where u.user_name =?1", Long.class).setParameter(1, name).getSingleResult().equals(1L);
    }


}
