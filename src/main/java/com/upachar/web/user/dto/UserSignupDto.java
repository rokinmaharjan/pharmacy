package com.upachar.web.user.dto;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupDto {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	@Size(max = 10, min = 10)
	private String phone;
	private String address;

}
