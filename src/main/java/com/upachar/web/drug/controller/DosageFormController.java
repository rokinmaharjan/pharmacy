package com.upachar.web.drug.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upachar.web.drug.domain.DosageForm;
import com.upachar.web.drug.dto.DosageFormDto;
import com.upachar.web.drug.service.DosageFormService;
import com.upachar.web.exceptionhandler.GlobalException;

@RestController
@RequestMapping("/dosage-forms")
public class DosageFormController {
	
	@Autowired
	private DosageFormService dosageFormService;
	
	@PostMapping
	public DosageForm addDosageForm(@RequestBody @Valid DosageFormDto dosageFormDto) throws GlobalException {
		return dosageFormService.addDosageForm(dosageFormDto);
	}
	
	@GetMapping
	public List<DosageForm> getAllDosageForms() {
		return dosageFormService.findAll();
	}

}
