package com.upachar.web.drug.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upachar.web.common.dto.PageDto;
import com.upachar.web.drug.domain.Drug;
import com.upachar.web.drug.dto.DrugDto;
import com.upachar.web.drug.service.DrugService;
import com.upachar.web.exceptionhandler.GlobalException;

@RestController
@RequestMapping("/drugs")
public class DrugController {

	@Autowired
	private DrugService drugService;

	@PostMapping
	public Drug addDrug(@RequestPart DrugDto drugDto, @RequestPart("image") MultipartFile image)
			throws GlobalException, IOException {
		return drugService.addDrug(drugDto, image);
	}

	@GetMapping
	public PageDto findDrugsWithPaging(@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer size) {
		return drugService.findDrugsWithPaging(page, size);
	}
	
	@GetMapping("/raw")
	public PageDto findAllDrugsWithPaging(@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer size) {
		return drugService.findAllDrugsWithPaging(page, size);
	}

	@GetMapping("/{drugId}")
	public Drug findDrugById(@PathVariable("drugId") Long drugId) throws GlobalException {
		return drugService.findById(drugId);
	}

	@PutMapping("/{drugId}")
	public Drug updateDrug(
			@PathVariable("drugId") Long drugId, 
			@RequestPart DrugDto drugDto,
			@RequestPart(value = "image", required = false) MultipartFile image) throws GlobalException, IOException {
		return drugService.updateDrug(drugId, drugDto, image);
	}
	
	@GetMapping("/sub-categories/{subCategoryId}")
	public List<Drug> findDrugsInSubCategory(@PathVariable Long subCategoryId) {
		return drugService.findBySubCategoryId(subCategoryId);
	}

	@GetMapping("/names")
	public Set<String> getAllDrugNames() {
		return drugService.getAllDrugNames();
	}

	@GetMapping("/special-offer")
	public List<Drug> getSpecialOfferDrugs() {
		return drugService.getSpecialOfferDrugs();
	}
	
	@GetMapping("/trending")
	public List<Drug> getTrendingDrugs() {
		return drugService.getTrendingDrugs();
	}
	
	@DeleteMapping("/{drugId}")
	public void deleteDrug(@PathVariable Long drugId) throws GlobalException, IOException {
		drugService.deleteDrug(drugId);
	}
	
	@GetMapping("/latest")
	public List<Drug> getLatestDrugs() {
		return drugService.getTop10LatestDrugs();
	}
	
	@GetMapping("/search")
	public List<Drug> searchDrugs(@RequestParam String search) {
		return drugService.findByBrandNameRegexOrGenericNameRegex(search, search);
	}

}
