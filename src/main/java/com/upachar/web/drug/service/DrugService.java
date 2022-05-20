package com.upachar.web.drug.service;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upachar.web.common.dto.PageDto;
import com.upachar.web.common.utils.CustomBeanUtils;
import com.upachar.web.common.utils.CustomStringUtils;
import com.upachar.web.common.utils.FileUtils;
import com.upachar.web.drug.domain.DosageForm;
import com.upachar.web.drug.domain.Drug;
import com.upachar.web.drug.domain.SubCategory;
import com.upachar.web.drug.dto.DrugDto;
import com.upachar.web.drug.repository.DrugRepository;
import com.upachar.web.exceptionhandler.GlobalException;
import com.upachar.web.order.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DrugService {
	
	@Value("${drug.images.path}")
	String drugImagesPath;
	
	@Value("${server.base.url}")
	String serverBaseUrl;

	@Autowired
	private DrugRepository drugRepository;

	@Autowired
	private SubCategoryService subCategoryService;
	
	@Autowired
	private DosageFormService dosageFormService;
	
	@Autowired
	private OrderService orderService;

	public Drug addDrug(DrugDto drugDto, MultipartFile image) throws GlobalException, IOException {
		String imageName = FileUtils.uploadImage(image, drugImagesPath);
	
		Drug drug = new Drug();

		CustomBeanUtils.copyNonNullProperties(drugDto, drug);

		SubCategory subCategory = subCategoryService.findById(drugDto.getSubCategoryId());
		if (drugDto.getDosageFormId() != null) {
			DosageForm dosageForm = dosageFormService.findById(drugDto.getDosageFormId());
			drug.setDosageForm(dosageForm);
		}

		drug.setSubCategory(subCategory);
		drug.setPhotoUrl(serverBaseUrl + imageName);

		return drugRepository.save(drug);
	}

	public PageDto findDrugsWithPaging(Integer page, Integer size) {
		Pageable pageRequest = PageRequest.of(page, size);
		Page<Drug> drugsPage = drugRepository.findByPriceGreaterThan(0, pageRequest);

		PageDto pageDto = new PageDto();
		pageDto.setContent(drugsPage.getContent());
		pageDto.setTotalElements(drugsPage.getTotalElements());
		pageDto.setTotalPages(drugsPage.getTotalPages());
		pageDto.setPageElementCount(drugsPage.getNumberOfElements());

		return pageDto;
	}
	
	public PageDto findAllDrugsWithPaging(Integer page, Integer size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Drug> drugsPage = drugRepository.findAll(pageRequest);

		PageDto pageDto = new PageDto();
		pageDto.setContent(drugsPage.getContent());
		pageDto.setTotalElements(drugsPage.getTotalElements());
		pageDto.setTotalPages(drugsPage.getTotalPages());
		pageDto.setPageElementCount(drugsPage.getNumberOfElements());

		return pageDto;
	}

	public Drug findById(Long drugId) throws GlobalException {
		return drugRepository.findById(drugId)
				.orElseThrow(() -> new GlobalException("Drug not found", HttpStatus.BAD_REQUEST));
	}

	public Drug updateDrug(Long drugId, DrugDto drugDto, MultipartFile image) throws GlobalException, IOException {
		Drug drug = findById(drugId);
		
		CustomBeanUtils.copyNonNullProperties(drugDto, drug);
		
		if (image != null) {
			// Delete previous image
			try {
				FileUtils.deleteImage(CustomStringUtils.getImageNameFromUrl(drug.getPhotoUrl()), drugImagesPath);
			} catch (NoSuchFileException e) {
				log.info(e.getMessage());
			} catch (IOException e) {
				throw e;
			}
			
			String imageName = FileUtils.uploadImage(image, drugImagesPath);
			drug.setPhotoUrl(serverBaseUrl + imageName);
		}
		
		if (drugDto.getSubCategoryId() != null) {
			SubCategory subCategory = subCategoryService.findById(drugDto.getSubCategoryId());
			drug.setSubCategory(subCategory);
		}
		
		if (drugDto.getDosageFormId() !=null) {
			DosageForm dosageForm = dosageFormService.findById(drugDto.getDosageFormId());
			drug.setDosageForm(dosageForm);
		}
		
		
		return drugRepository.save(drug);
	}

	public Set<String> getAllDrugNames() {
		return drugRepository.findAll().stream().map(Drug::getGenericName).collect(Collectors.toSet());
	}

	public List<Drug> findBySubCategoryId(Long categoryId) {
		return drugRepository.findBySubCategoryId(categoryId);
	}

	public List<Drug> getSpecialOfferDrugs() {
		return drugRepository.findBySpecialOffer(true);
	}

	public List<Drug> getTrendingDrugs() {
		return drugRepository.findByTrending(true);
	}

	public void deleteDrug(Long drugId) throws GlobalException, IOException {
		Drug drug = findById(drugId);
		log.info("Deleting drug '{}'", drug.getGenericName());
		
		try {
			FileUtils.deleteImage(CustomStringUtils.getImageNameFromUrl(drug.getPhotoUrl()), drugImagesPath);
		} catch (NoSuchFileException e) {
			log.info(e.getMessage());
		} catch (IOException e) {
			throw e;
		}
		
		log.info("Deleting all orders associated with drug '{}'", drug.getGenericName());
		orderService.deleteAllAssociatedWithDrug(drugId);
		
		drugRepository.delete(drug);
	}

	public void deleteAllAssociatedToSubCategory(Long subCategoryId) {
		List<Drug> drugs = findBySubCategoryId(subCategoryId);
		
		drugRepository.deleteAll(drugs);
	}

	public List<Drug> getTop10LatestDrugs() {
		return drugRepository.findTop10ByOrderByCreatedDateDesc();
	}

	public List<Drug> findByBrandNameRegexOrGenericNameRegex(String brandNameRegex, String genericNameRegex) {
		brandNameRegex = "%" + brandNameRegex + "%";
		genericNameRegex = "%" + genericNameRegex + "%";
		return drugRepository.findByBrandNameLikeOrGenericNameLike(brandNameRegex, genericNameRegex);
	}


}
