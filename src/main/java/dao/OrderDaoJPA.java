package dao;

import model.OrderJPA;
import model.UserJPA;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class OrderDaoJPA {

   @PersistenceContext
   public EntityManager em;

   public List<OrderJPA> findOrders() {
      return em.createQuery("select " +
              "o from OrderJPA o").getResultList();
   }


   public OrderJPA findOrderById(long id) {
      TypedQuery<OrderJPA> query = em.createQuery(
              "select o from OrderJPA o " +
              "where o.id = :id",
              OrderJPA.class);
      query.setParameter("id", id);
      return query.getSingleResult();
   }

   @Transactional
   public OrderJPA insertOrder(OrderJPA order) {
      if (order.getId() == null) {
         em.persist(order);
      } else {
         em.merge(order);
      }
      return order;
   }


   public boolean deleteOrder(Long id) {
      OrderJPA order = em.find(OrderJPA.class, id);
      if (order == null) {
         return false;
      }
      em.remove(order);
      return true;
   }



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