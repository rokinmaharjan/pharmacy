package com.upachar.web.order.dto;

import com.upachar.web.order.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDto {
	private OrderStatus orderStatus;
}
