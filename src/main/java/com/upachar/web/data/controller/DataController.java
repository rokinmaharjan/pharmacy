package com.upachar.web.data.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upachar.web.data.service.DataService;
import com.upachar.web.exceptionhandler.GlobalException;

@RestController
@RequestMapping("/data")
public class DataController {
	
	@Autowired
	DataService dataService;
	
	@PostMapping
	public String loadData(@RequestParam MultipartFile file) throws IllegalStateException, IOException, GlobalException {
		dataService.loadDataFromFile(file);
		return "Successfully loaded data from file";
	}

}
