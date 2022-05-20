package com.upachar.web.user.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.upachar.web.common.dto.PageDto;
import com.upachar.web.common.utils.CustomBeanUtils;
import com.upachar.web.exceptionhandler.GlobalException;
import com.upachar.web.order.service.OrderService;
import com.upachar.web.user.domain.User;
import com.upachar.web.user.domain.UserRole;
import com.upachar.web.user.dto.UserSignupDto;
import com.upachar.web.user.dto.UserUpdateDto;
import com.upachar.web.user.dto.UserValidateDto;
import com.upachar.web.user.enums.Role;
import com.upachar.web.user.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private OrderService orderService;

	public User addUser(User user) {
		return userRepository.save(user);
	}

	public User findById(Long userId) throws GlobalException {
		return userRepository.findById(userId)
				.orElseThrow(() -> new GlobalException("User not found", HttpStatus.BAD_REQUEST));
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public PageDto findUsersWithPaging(Integer page, Integer size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<User> usersPage = userRepository.findAll(pageRequest);

		PageDto pageDto = new PageDto();
		pageDto.setContent(usersPage.getContent());
		pageDto.setTotalElements(usersPage.getTotalElements());
		pageDto.setTotalPages(usersPage.getTotalPages());
		pageDto.setPageElementCount(usersPage.getNumberOfElements());

		return pageDto;
	}

	public User signupUser(UserSignupDto userSignupDto) throws GlobalException {
		validateIfUserIsUnique(userSignupDto.getEmail(), userSignupDto.getPhone());
		validateEmail(userSignupDto.getEmail());
		
		User user = new User();
		CustomBeanUtils.copyNonNullProperties(userSignupDto, user);

		UserRole userRole = new UserRole(Role.USER);
		user.setRoles(Arrays.asList(userRole));
		user.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));

		return userRepository.save(user);
	}
	
	private boolean validateEmail(String email) throws GlobalException {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		
		if (email != null && !email.matches(regex)) {
			throw new GlobalException("Email format incorrect", HttpStatus.BAD_REQUEST);
		}
		
		return true;
	}

	private boolean validateIfUserIsUnique(String email, String phone) throws GlobalException {
		if (email != null) {
			User user = userRepository.findByEmail(email);
			
			if (user != null) {
				throw new GlobalException("Email already registered", HttpStatus.BAD_REQUEST);
			}
		}
		
		User user = userRepository.findByPhone(phone);

		if (user != null) {
			throw new GlobalException("Phone number already registered", HttpStatus.BAD_REQUEST);
		}

		return true;
	}

	public User signupAdmin(UserSignupDto userSignupDto) {
		User user = new User();
		CustomBeanUtils.copyNonNullProperties(userSignupDto, user);

		UserRole userRole = new UserRole(Role.ADMIN);
		user.setRoles(Arrays.asList(userRole));
		user.setPassword(passwordEncoder.encode(userSignupDto.getPassword()));

		return userRepository.save(user);
	}

	public User updateUser(Long userId, UserUpdateDto userUpdateDto) throws GlobalException {
		validateIfUserIsUpdateable(userId, userUpdateDto.getEmail(), userUpdateDto.getPhone());
		
		User user = findById(userId);
		
		if (!userUpdateDto.getEmail().equals("")) {
			validateEmail(userUpdateDto.getEmail());
			
			CustomBeanUtils.copyNonNullProperties(userUpdateDto, user);
		} else {
			CustomBeanUtils.copyNonNullProperties(userUpdateDto, user);
			user.setEmail(null);
		}
		
		return userRepository.save(user);
	}

	private boolean validateIfUserIsUpdateable(Long userId, String email, String phone) throws GlobalException {
		if (email != null) {
			User user = findByEmailAndIdNot(email, userId);
	
			if (user != null) {
				throw new GlobalException("Email already registered", HttpStatus.BAD_REQUEST);
			}
		}

		User user = findByIdNotAndPhone(userId, phone);

		if (user != null) {
			throw new GlobalException("Phone number already registered", HttpStatus.BAD_REQUEST);
		}

		return true;
	}
	
	public User findByEmailAndIdNot(String email, Long userId) {
		return userRepository.findByEmailAndIdNot(email, userId);
	}
	
	public User findByIdNotAndPhone(Long userId, String phone) {
		return userRepository.findByIdNotAndPhone(userId, phone);
	}

	public void deleteUser(Long userId) {
		orderService.deleteAllAssociatedWithUser(userId);
		
		userRepository.deleteById(userId);
	}

	public void checkIfUserCreateable(UserValidateDto userValidateDto) throws GlobalException {
		validateIfUserIsUnique(userValidateDto.getEmail(), userValidateDto.getPhone());
		validateEmail(userValidateDto.getEmail());
	}

}
