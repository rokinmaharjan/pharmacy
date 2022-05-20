package com.upachar.web.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upachar.web.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	User findByPhone(String phone);

	User findByEmailAndIdNot(String email, Long userId);

	User findByIdNotAndPhone(Long userId, String phone);
}
