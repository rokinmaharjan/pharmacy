package com.upachar.web.order.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private Map<Long, Integer> drugQuantities;
	private Double totalPrice;
	private Long userId;
}
