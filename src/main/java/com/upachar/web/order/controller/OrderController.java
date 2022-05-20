package com.upachar.web.order.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upachar.web.common.dto.PageDto;
import com.upachar.web.exceptionhandler.GlobalException;
import com.upachar.web.order.domain.Order;
import com.upachar.web.order.dto.OrderDto;
import com.upachar.web.order.dto.OrderStatusDto;
import com.upachar.web.order.dto.OrderUpdateDto;
import com.upachar.web.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping
	public Order addOrder(@RequestPart OrderDto orderDto,
			@RequestPart(value = "prescriptions", required = false) MultipartFile[] prescriptions)
			throws GlobalException, IOException {
		return orderService.addOrders(orderDto, prescriptions);
	}
	
	@GetMapping
	public PageDto findOrdersWithPaging(@RequestParam(required = true) Integer page,
			@RequestParam(required = true) Integer size) {
		return orderService.findOrdersWithPaging(page, size);
	}
	
	@GetMapping("/users/{userId}")
	public List<Order> findOrdersByUser(@PathVariable Long userId) {
		return orderService.findByUserId(userId);
	}
	
	@PostMapping("/prescriptions")
	public Order orderWithPrescription(@RequestPart OrderDto orderDto, @RequestPart MultipartFile[] prescriptions)
			throws IOException, GlobalException {
		return orderService.orderWithPrescription(orderDto, prescriptions);
	}
	
	@PutMapping("/{orderId}")
	public Order editOrder(@PathVariable Long orderId, @RequestBody OrderUpdateDto orderUpdateDto) throws GlobalException {
		return orderService.updateOrder(orderId, orderUpdateDto);
	}
	
	@DeleteMapping("/{orderId}")
	public void deleteOrder(@PathVariable Long orderId) {
		orderService.deleteOrder(orderId);
	}
	
	@PatchMapping("/{orderId}/status")
	public Order updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderStatusDto orderStatusDto) throws GlobalException {
		return orderService.updateOrderStatus(orderId, orderStatusDto);
	}
	
	@GetMapping("/{orderId}")
	public Order getOrderById(@PathVariable Long orderId) throws GlobalException {
		return orderService.findById(orderId);
	}
	
	@PostMapping("/{orderId}/reorder")
	public Order reorderOrder(@PathVariable Long orderId, @RequestBody OrderDto orderDto) throws GlobalException {
		return orderService.reorderOrder(orderId, orderDto);
	}

}
