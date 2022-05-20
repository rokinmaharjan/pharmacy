package com.upachar.web.drug.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dosage_forms")
@EntityListeners(AuditingEntityListener.class)
public class DosageForm {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String form;
	
	public DosageForm(String form) {
		this.form = form;
	}

}
