package com.upachar.web.drug.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upachar.web.drug.domain.DosageForm;

public interface DosageFormRepository extends JpaRepository<DosageForm, Long> {

	DosageForm findByForm(String form);

}
