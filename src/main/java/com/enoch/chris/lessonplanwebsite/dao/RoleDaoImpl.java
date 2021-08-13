package com.enoch.chris.lessonplanwebsite.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.enoch.chris.lessonplanwebsite.entity.Role;


@Repository
public class RoleDaoImpl implements RoleDao {

	private SessionFactory sessionFactory;
	
	@Autowired
	public RoleDaoImpl(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Role findRoleByName(String theRoleName) {

		Session currentSession = sessionFactory.getCurrentSession();

		Query<Role> theQuery = currentSession.createQuery("from Role where authority=:roleName", Role.class);
		theQuery.setParameter("roleName", theRoleName);
		
		Role theRole = null;
		
		try {
			theRole = theQuery.getSingleResult();
		} catch (Exception e) {
			theRole = null;
		}
		
		return theRole;
	}
}
