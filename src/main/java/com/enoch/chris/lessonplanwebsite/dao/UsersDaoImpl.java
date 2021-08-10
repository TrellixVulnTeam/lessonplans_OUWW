 package com.enoch.chris.lessonplanwebsite.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.controller.entity.User;

@Repository
public class UsersDaoImpl implements UsersDao{
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public UsersDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	@Override //@Transactional - functionality moved up to service layer
	public List<User> getMembers(int pageStart, int recordsPerPage) {
		
		Session currentSession = sessionFactory.getCurrentSession();

			// create a query  ... sort by last name
			Query<User> theQuery = 
					currentSession.createQuery("from Users order by firstName",
												User.class)
					.setFirstResult(pageStart)
					.setMaxResults(recordsPerPage);
	
		 	List<User> members = theQuery.getResultList();
		 	
		 	members.forEach(System.out::println);
	
			return members;	
		
	}
	
	
	@Override
	public int getTotalMembers() {
		
		Session currentSession = sessionFactory.getCurrentSession();

			Query<User> theQuery = 
					currentSession.createQuery("from Users",
												User.class);

		 	List<User> members = theQuery.getResultList();
		 	int total = members.size();
		 			
			return total;
		
	}
		
	@Override
	public User getUserByUsername(String username) throws NoResultException {		
		Session currentSession = sessionFactory.getCurrentSession();
		
		//Try statement in case invalid username entered or other problem.
		try {
			String sqlQuery = "from Users where username=:username"; 

			Query<User> theQuery = 
					currentSession.createQuery(sqlQuery,
												User.class);
			
			theQuery.setParameter("username", username);
			
			User theUser;
			theUser = theQuery.getSingleResult();
			
			return theUser;
		} catch(Exception exc) {
			throw exc;
		} 
			
	}

	@Override
	public User getUserByEmail(String email)  {		
		Session currentSession = sessionFactory.getCurrentSession();
		
			String sqlQuery = "from Users where email=:email"; 

			Query<User> theQuery = 
					currentSession.createQuery(sqlQuery,
												User.class);	
			theQuery.setParameter("email", email);
			
			User theUser;
			theUser = theQuery.getSingleResult();

			return theUser;
		
	}

	@Override
	public void save(User theUser) {
				Session currentSession = sessionFactory.getCurrentSession();

				currentSession.saveOrUpdate(theUser);
		
	}
	
	@Override
	public void delete(User theUser) {
				Session currentSession = sessionFactory.getCurrentSession();

				currentSession.delete(theUser);
		
	}
	
}
