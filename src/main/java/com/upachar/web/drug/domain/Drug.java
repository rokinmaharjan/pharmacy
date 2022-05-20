package com.upachar.web.drug.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "drugs")
@EntityListeners(AuditingEntityListener.class)
public class Drug {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long productId;
	private String brandName;
	private String genericName;
	private String manufacturer;
	
	@OneToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } )
	private DosageForm dosageForm;
	
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH } )
	private SubCategory subCategory;
	
	private String details;
	private Boolean inStock;
	private String photoUrl;
	private Double price;
	private Integer packSize;
	private Boolean prescriptionRequired;
	private Boolean trending;
	private Boolean specialOffer;
	
	@JsonProperty(access = Access.READ_ONLY)
	@CreatedDate
	private Date createdDate;
	@JsonProperty(access = Access.READ_ONLY)
	@LastModifiedDate
	private Date modifiedDate;

}
