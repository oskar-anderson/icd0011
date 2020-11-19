package dao;

import model.UserJPA;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDaoJPA {

   @PersistenceContext
   private EntityManager em;

   public List<UserJPA> findUsers() {
      return em.createQuery("select " +
              "o from UserJPA o").getResultList();
   }

   public UserJPA findUserByUsername(String username) {
      TypedQuery<UserJPA> query = em.createQuery(
              "select o from UserJPA o " +
              "where o.username = :username",
              UserJPA.class);
      query.setParameter("username", username);
      return query.getSingleResult();
   }

   public void hashPassword(UserJPA user){
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
      String hash = encoder.encode(user.getPassword());
      user.buildPassword(hash);
   }

   @Transactional
   public UserJPA insertUser(UserJPA user) {
      if (user.getUsername() == null) {
         em.persist(user);
      } else {
         em.merge(user);
      }
      return user;
   }


   public boolean deleteUser(String username) {
      UserJPA user = em.find(UserJPA.class, username);
      if (user == null) {
         return false;
      }
      em.remove(user);
      return true;
   }
}
