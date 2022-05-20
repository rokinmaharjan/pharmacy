package com.upachar.web.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upachar.web.order.domain.DrugQuantity;

public interface DrugQuantityRepository extends JpaRepository<DrugQuantity, Long> {

	public List<DrugQuantity> findByDrugId(Long drugId);
}
