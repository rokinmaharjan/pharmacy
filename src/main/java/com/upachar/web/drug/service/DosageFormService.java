package com.upachar.web.drug.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.upachar.web.common.utils.CustomBeanUtils;
import com.upachar.web.drug.domain.DosageForm;
import com.upachar.web.drug.dto.DosageFormDto;
import com.upachar.web.drug.repository.DosageFormRepository;
import com.upachar.web.exceptionhandler.GlobalException;

@Service
public class DosageFormService {
	@Autowired
	private DosageFormRepository dosageFormRepository;

	public DosageForm addDosageForm(DosageFormDto dosageFormDto) throws GlobalException {
		DosageForm dosageForm = dosageFormRepository.findByForm(dosageFormDto.getForm());

		if (dosageForm != null) {
			throw new GlobalException("Dosage form already exists", HttpStatus.BAD_REQUEST);
		}

		dosageForm = new DosageForm();

		CustomBeanUtils.copyNonNullProperties(dosageFormDto, dosageForm);

		return dosageFormRepository.save(dosageForm);
	}

	public List<DosageForm> findAll() {
		return dosageFormRepository.findAll();
	}

	public List<DosageForm> addAll(List<DosageForm> dosageForms) {
		return dosageFormRepository.saveAll(dosageForms);
	}

	public DosageForm findById(Long dosageFormId) throws GlobalException {
		return dosageFormRepository.findById(dosageFormId)
				.orElseThrow(() -> new GlobalException("Dosage form does not exist", HttpStatus.BAD_REQUEST));
	}

}
