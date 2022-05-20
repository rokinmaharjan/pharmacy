package com.upachar.web.user.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upachar.web.common.dto.PageDto;
import com.upachar.web.exceptionhandler.GlobalException;
import com.upachar.web.user.domain.User;
import com.upachar.web.user.dto.UserSignupDto;
import com.upachar.web.user.dto.UserUpdateDto;
import com.upachar.web.user.dto.UserValidateDto;
import com.upachar.web.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping()
	public PageDto findUsersWithPaging(@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer size) {
		return userService.findUsersWithPaging(page, size);
	}
	
	@PostMapping("/signup")
	public User signupUser(@RequestBody @Valid UserSignupDto userSignupDto) throws GlobalException {
		return userService.signupUser(userSignupDto);
	}
	
	@PostMapping("/admin/signup")
	public User signupAdmin(@RequestBody UserSignupDto userSignupDto) {
		return userService.signupAdmin(userSignupDto);
	}
	
	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Long userId) throws GlobalException {
		return userService.findById(userId);
	}
	
	@PutMapping("/{userId}")
	public User updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) throws GlobalException {
		return userService.updateUser(userId, userUpdateDto);
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
	}
	
	@PostMapping("/validate")
	public Map<String, Boolean> checkIfUserCreateable(@RequestBody @Valid UserValidateDto userValidateDto) throws GlobalException {
		userService.checkIfUserCreateable(userValidateDto);
		
		Map<String, Boolean> validationMap = new HashMap<>();
		validationMap.put("valid", true);
		
		return validationMap;
	}
}
