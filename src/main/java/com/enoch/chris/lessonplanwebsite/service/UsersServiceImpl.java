package com.enoch.chris.lessonplanwebsite.service;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.enoch.chris.lessonplanwebsite.dao.RoleRepository;
import com.enoch.chris.lessonplanwebsite.dao.UserRepository;
import com.enoch.chris.lessonplanwebsite.entity.Role;
import com.enoch.chris.lessonplanwebsite.entity.User;
import com.enoch.chris.lessonplanwebsite.registration.user.RegistrationUser;


@Service
public class UsersServiceImpl implements UsersService{
	
private RoleRepository roleRepository;
private UserRepository userRepository;


@Autowired
private BCryptPasswordEncoder passwordEncoder;


	@Autowired
	public UsersServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	
	@Override
	@Transactional
	public void save(RegistrationUser regUser) {
		User user = new User();
		 // assign user details to the user object
		user.setUsername(regUser.getUserName());
		user.setPassword(passwordEncoder.encode(regUser.getPassword()));
		user.setEmail(regUser.getEmail());
		user.setEnabled((byte)1);

		// give user default role of "employee"
		user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_CUSTOMER")));
		
		userRepository.save(user);

	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password."); 
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
	
	
	
	
//	@Override
//	@Transactional
//	public void delete(User theUser) {
//		usersDAO.delete(theUser);
//	}
//
//	@Override
//	@Transactional
//	public User getUserByEmail(String email) {
//		return usersDAO.getUserByEmail(email);
//	}
//	
//	@Override
//	@Transactional
//	public List<User> getMembers(int pageStart, int recordsPerPage) {
//
//		return usersDAO.getMembers(pageStart, recordsPerPage);
//	}
//
//
//
//	@Override
//	@Transactional
//	public int getTotalMembers() {
//
//		return usersDAO.getTotalMembers();
//	}
//
//	@Override
//	@Transactional
//	public User getUserByUsername(String username) {
//		return usersDAO.getUserByUsername(username);
//	}
	

}
