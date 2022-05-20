package com.upachar.web.data.dto;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class DrugDetailsDto {
	@CsvBindByPosition(position = 1)
	private String brandName;

	@CsvBindByPosition(position = 2)
	private String genericName;

	@CsvBindByPosition(position = 3)
	private String prescriptionRequired;
	
	@CsvBindByPosition(position = 4)
	private String dosageForm;
	
	@CsvBindByPosition(position = 5)
	private String manufacturer;
	
	@CsvBindByPosition(position = 6)
	private Double price;
	
	@CsvBindByPosition(position = 7)
	private String details;
	
	@CsvBindByPosition(position = 8)
	private String subCategory;
	
	@CsvBindByPosition(position = 10)
	private int packSize;


}
