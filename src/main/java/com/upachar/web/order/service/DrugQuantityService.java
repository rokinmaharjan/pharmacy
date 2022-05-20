package com.upachar.web.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.upachar.web.order.domain.DrugQuantity;
import com.upachar.web.order.repository.DrugQuantityRepository;

public class DrugQuantityService {
	
	@Autowired
	private DrugQuantityRepository drugQuantityRepository;
	
	public List<DrugQuantity> findByDrugId(Long drugId) {
		return drugQuantityRepository.findByDrugId(drugId);
	}

}
