package com.upachar.web.drug.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrugDto {
	private Long productId;
	private String brandName;
	private String genericName;
	private Long dosageFormId;
	private String manufacturer;
	private Long subCategoryId;
	private String details;
	private Boolean inStock;
	private Double price;
	private Integer packSize;
	private Boolean prescriptionRequired;
	private Boolean trending;
	private Boolean specialOffer;
}
