package com.upachar.web.order.domain;

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
@Table(name = "order_prescriptions")
@EntityListeners(AuditingEntityListener.class)
public class OrderPrescription {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String prescriptionUrl;

	public OrderPrescription(String prescriptionUrl) {
		super();
		this.prescriptionUrl = prescriptionUrl;
	}

}
