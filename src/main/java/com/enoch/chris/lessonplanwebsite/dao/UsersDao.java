package com.enoch.chris.lessonplanwebsite.dao;

import java.util.List;

import com.enoch.chris.lessonplanwebsite.entity.User;


 public interface UsersDao {
	
	 List<User> getMembers(int pageStart, int recordsPerPage);
	 int getTotalMembers();
	 User getUserByUsername(String username);
	 void save(User theUser); 
	 void delete(User theUser);
	 User getUserByEmail(String email);  

}
