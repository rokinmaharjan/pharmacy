package com.upachar.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.upachar.web.drug.domain.DosageForm;
import com.upachar.web.drug.service.DosageFormService;
import com.upachar.web.user.domain.User;
import com.upachar.web.user.domain.UserRole;
import com.upachar.web.user.enums.Role;
import com.upachar.web.user.service.UserService;

@EnableJpaAuditing
@SpringBootApplication
public class UpacharWebApplication extends SpringBootServletInitializer implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private DosageFormService dosageFormService;

	public static void main(String[] args) {
		SpringApplication.run(UpacharWebApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UpacharWebApplication.class);
	}
	
	@Override
	public void run(String... args) throws Exception {
		// Add default user
		User user = userService.findByEmail("rokinmaharjan@gmail.com");
		
		if (user == null) {
			UserRole role1 = new UserRole(Role.ADMIN);
			UserRole role2 = new UserRole(Role.USER); 
			
			user = User.builder()
					.email("rokinmaharjan@gmail.com")
					.password(passwordEncoder.encode("rokin123"))
					.firstName("Rokin")
					.lastName("Maharjan")
					.phone("9808621458")
					.address("Kirtipur")
					.roles(Arrays.asList(role1, role2))
					.build();
			
			userService.addUser(user);
		}
		
		
		// Add default dosage form list
		List<DosageForm> dosageForms = dosageFormService.findAll();
		
		if (dosageForms.isEmpty()) {
			dosageForms = Arrays.asList(
					new DosageForm("Tablet"),
					new DosageForm("Injection"),
					new DosageForm("Cream"),
					new DosageForm("Ointment Topical"),
					new DosageForm("Ointment Eye"),
					new DosageForm("Syrup"),
					new DosageForm("Suspension"),
					new DosageForm("Capsule"),
					new DosageForm("Inhaler/Aerosol"),
					new DosageForm("Drop Oral"),
					new DosageForm("Drop Nasal"),
					new DosageForm("Drop Ear"),
					new DosageForm("Drop Eye"),
					new DosageForm("Nasal Spray"),
					new DosageForm("Suppository Rectal"),
					new DosageForm("Suppository Vaginal")
				);
			
			dosageFormService.addAll(dosageForms);
		}
	}
	
}
