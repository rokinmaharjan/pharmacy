package com.upachar.web.drug.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.upachar.web.drug.domain.Drug;

public interface DrugRepository extends JpaRepository<Drug, Long>{

	List<Drug> findBySubCategoryId(Long categoryId);

	List<Drug> findBySpecialOffer(Boolean specialOffer);

	List<Drug> findByTrending(Boolean trending);

	List<Drug> findTop10ByOrderByCreatedDateDesc();

	List<Drug> findByBrandNameLikeOrGenericNameLike(String brandNameRegex, String genericNameRegex);

	Page<Drug> findByPriceGreaterThan(double price, Pageable pageRequest);

}
