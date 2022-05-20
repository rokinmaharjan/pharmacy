package com.upachar.web.data.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBeanBuilder;
import com.upachar.web.data.dto.DrugDetailsDto;
import com.upachar.web.drug.domain.Category;
import com.upachar.web.drug.domain.DosageForm;
import com.upachar.web.drug.domain.Drug;
import com.upachar.web.drug.domain.SubCategory;
import com.upachar.web.drug.repository.CategoryRepository;
import com.upachar.web.drug.repository.DrugRepository;
import com.upachar.web.drug.repository.SubCategoryRepository;
import com.upachar.web.drug.service.DosageFormService;
import com.upachar.web.exceptionhandler.GlobalException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataService {

	@Autowired
	private DosageFormService dosageFormService;
	
	@Autowired
	private DrugRepository drugRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private SubCategoryRepository subCategoryRepository;
	
	private static final String PRESCRIPTIONS = "Prescriptions";
	

	public void loadDataFromFile(MultipartFile multipartFile) throws IOException, GlobalException {
		Path filepath = Paths.get(multipartFile.getOriginalFilename());
		multipartFile.transferTo(filepath);
		
		File file = filepath.toFile();
		
		// OPEN CSV BEAN STYLE
		List<DrugDetailsDto> drugs = new CsvToBeanBuilder<DrugDetailsDto>(new FileReader(file))
				.withType(DrugDetailsDto.class).build().parse();
		
		
		Category category = categoryRepository.findByName(PRESCRIPTIONS);
		if (category == null) {
			category = Category.builder()
					.name(PRESCRIPTIONS)
					.photoUrl("https://i.ytimg.com/vi/2QvOxa_7wEw/maxresdefault.jpg")
					.build();

			category = categoryRepository.save(category);
		}

		long counter = 1;
		for (DrugDetailsDto d : drugs) {
			log.info("Processing drug {} with product id {}", d.getBrandName(), counter);
			
			if (d.getBrandName() == null || d.getBrandName().isEmpty()) {
				log.info("Found drug without brand name. Skipping drug.");
				continue;
			}
			
			boolean prescriptionRequired = false;
			if (d.getPrescriptionRequired().equals("yes")) {
				prescriptionRequired = true;
			}
			
			SubCategory subCategory = subCategoryRepository.findByName(d.getSubCategory());
			if (subCategory == null) {
				subCategory = SubCategory.builder()
						.name(d.getSubCategory())
						.category(category)
						.build();

				subCategory = subCategoryRepository.save(subCategory);
			}
			
			
			DosageForm dosageForm = null;
			try {
				dosageForm = getDosageForm(d.getDosageForm());
			} catch (GlobalException e) {
				e.printStackTrace();
				
				System.err.println("-------------------------------------------------");
				System.err.println(d.toString());
				throw e;
			}
			
			String details = d.getDetails().replaceAll("rn", "\n");
			Drug drug = Drug.builder()
					.productId(counter)
					.brandName(d.getBrandName())
					.genericName(d.getGenericName())
					.subCategory(subCategory)
					.dosageForm(dosageForm)
					.details(details)
					.inStock(true)
					.manufacturer(d.getManufacturer())
					.packSize(d.getPackSize())
					.prescriptionRequired(prescriptionRequired)
					.price(d.getPrice())
					.specialOffer(false)
					.trending(false)
					.photoUrl("https://thedailycougar.com/wp-content/uploads/2019/09/DE4DFD9C-2926-4BBC-8100-A64D0C3A64D6.png")
					.build();
			
			drugRepository.save(drug);
			
			counter++;
		}
		
		file.delete();
	}

	private DosageForm getDosageForm(String dosageForm) throws GlobalException {
		List<DosageForm> dosageForms = dosageFormService.findAll();

		Map<String, DosageForm> dosageFormMap = new HashMap<>();
		for (DosageForm df : dosageForms) {
			dosageFormMap.put(df.getForm(), df);
		}

		if (dosageForm.toLowerCase().contains("tablet")) {
			return dosageFormMap.get("Tablet");
			
		} else if (dosageForm.toLowerCase().contains("cream")) {
			return dosageFormMap.get("Cream");
			
		} else if (dosageForm.toLowerCase().contains("capsule")) {
			return dosageFormMap.get("Capsule");
		}

		else if (dosageForm.toLowerCase().contains("drop_ear") || dosageForm.contains("eye drops")) {
			return dosageFormMap.get("Drop Ear");
		}

		else if (dosageForm.toLowerCase().contains("drop_eye")) {
			return dosageFormMap.get("Drop Eye");
		}

		else if (dosageForm.toLowerCase().contains("drop_nasal")) {
			return dosageFormMap.get("Drop Nasal");
		}

		else if (dosageForm.toLowerCase().contains("drop_oral")) {
			return dosageFormMap.get("Drop Oral");
		}

		else if (dosageForm.toLowerCase().contains("inhaler_aerosol")) {
			return dosageFormMap.get("Inhaler/Aerosol");
		}

		else if (dosageForm.toLowerCase().contains("injection")) {
			return dosageFormMap.get("Injection");
		}

		else if (dosageForm.toLowerCase().contains("nasal spray") || dosageForm.contains("nasal_spray")) {
			return dosageFormMap.get("Nasal Spray");
		}

		else if (dosageForm.toLowerCase().contains("ointment_eye")) {
			return dosageFormMap.get("Ointment Eye");
		}

		else if (dosageForm.toLowerCase().contains("ointment_topical")) {
			return dosageFormMap.get("Ointment Topical");
		}

		else if (dosageForm.toLowerCase().contains("suspension")) {
			return dosageFormMap.get("Suspension");
		}

		else if (dosageForm.toLowerCase().contains("suppository_rectal")) {
			return dosageFormMap.get("Suppository Rectal");
		}

		else if (dosageForm.toLowerCase().contains("suppository_vaginal")) {
			return dosageFormMap.get("Suppository Vaginal");
		}

		else if (dosageForm.toLowerCase().contains("syrup")) {
			return dosageFormMap.get("Syrup");
		}
		
		throw new GlobalException("Dosage Form " + dosageForm + " not found", HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
