package com.upachar.web.order.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upachar.web.common.dto.PageDto;
import com.upachar.web.common.utils.CustomBeanUtils;
import com.upachar.web.common.utils.FileUtils;
import com.upachar.web.drug.domain.Drug;
import com.upachar.web.drug.service.DrugService;
import com.upachar.web.exceptionhandler.GlobalException;
import com.upachar.web.order.domain.DrugQuantity;
import com.upachar.web.order.domain.Order;
import com.upachar.web.order.domain.OrderPrescription;
import com.upachar.web.order.dto.OrderDto;
import com.upachar.web.order.dto.OrderStatusDto;
import com.upachar.web.order.dto.OrderUpdateDto;
import com.upachar.web.order.enums.OrderStatus;
import com.upachar.web.order.repository.DrugQuantityRepository;
import com.upachar.web.order.repository.OrderRepository;
import com.upachar.web.user.domain.User;
import com.upachar.web.user.service.UserService;

@Service
public class OrderService {
	
	@Value("${prescription.images.path}")
	String prescriptionImagesPath;
	
	@Value("${server.base.url}")
	String serverBaseUrl;
	
	@Autowired
	private DrugService drugService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private DrugQuantityRepository drugQuantityRepository;

	public Order addOrders(OrderDto orderDto, MultipartFile[] prescriptions) throws GlobalException, IOException {
		Set<DrugQuantity> drugQuantities = new HashSet<>();
		for (Map.Entry<Long, Integer> quantity : orderDto.getDrugQuantities().entrySet()) {
			Drug drug = drugService.findById(quantity.getKey());
			
			if (drug.getPrescriptionRequired() && prescriptions.length == 0) {
				throw new GlobalException("Drug '" + drug.getGenericName() + "' requires a prescription.", HttpStatus.BAD_REQUEST);
				
			}
			
			DrugQuantity drugQuantity = new DrugQuantity(quantity.getValue(), drug);
			drugQuantities.add(drugQuantity);
		}
		
		Order order = new Order();
		CustomBeanUtils.copyNonNullProperties(orderDto, order);
		
		User user = userService.findById(orderDto.getUserId());
		
		order.setDrugQuantities(drugQuantities);
		order.setUser(user);
		order.setDeliveryAddress(user.getAddress());
		order.setDeliveryPhone(user.getPhone());
		
		Set<OrderPrescription> prescriptionUrls = new HashSet<>();
		for (MultipartFile prescription: prescriptions) {
			String imageName = FileUtils.uploadImage(prescription, prescriptionImagesPath);
			
			OrderPrescription orderPrescription = new OrderPrescription(serverBaseUrl + imageName);
			prescriptionUrls.add(orderPrescription);
		}
		
		order.setOrderPrescriptions(prescriptionUrls);
		order.setOrderStatus(OrderStatus.PENDING);
		
		return orderRepository.save(order);
		
	}

	public PageDto findOrdersWithPaging(Integer page, Integer size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<Order> ordersPage = orderRepository.findAll(pageRequest);

		PageDto pageDto = new PageDto();
		pageDto.setContent(ordersPage.getContent());
		pageDto.setTotalElements(ordersPage.getTotalElements());
		pageDto.setTotalPages(ordersPage.getTotalPages());
		pageDto.setPageElementCount(ordersPage.getNumberOfElements());

		return pageDto;
	}

	public List<Order> findByUserId(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	public Order orderWithPrescription(OrderDto orderDto, MultipartFile[] prescriptions) throws IOException, GlobalException {
		Set<OrderPrescription> prescriptionUrls = new HashSet<>();
		for (MultipartFile prescription: prescriptions) {
			String imageName = FileUtils.uploadImage(prescription, prescriptionImagesPath);
			
			OrderPrescription orderPrescription = new OrderPrescription(serverBaseUrl + imageName);
			prescriptionUrls.add(orderPrescription);
		}
		
		Order order = new Order();
		CustomBeanUtils.copyNonNullProperties(orderDto, order);
		
		User user = userService.findById(orderDto.getUserId());
		
		order.setOrderPrescriptions(prescriptionUrls);
		order.setUser(user);
		order.setDeliveryAddress(user.getAddress());
		order.setDeliveryPhone(user.getPhone());
		order.setOrderStatus(OrderStatus.PENDING);
		
		return orderRepository.save(order);
	}

	public Order updateOrder(Long orderId, OrderUpdateDto orderUpdateDto) throws GlobalException {
		Order order = findById(orderId);
		
		CustomBeanUtils.copyNonNullProperties(orderUpdateDto, order);
		
		return orderRepository.save(order);
	}
	
	public Order findById(Long orderId) throws GlobalException {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new GlobalException("Order not found", HttpStatus.BAD_REQUEST));
	}

	public void deleteOrder(Long orderId) {
		orderRepository.deleteById(orderId);
	}

	public Order updateOrderStatus(Long orderId, OrderStatusDto orderStatusDto) throws GlobalException {
		Order order = findById(orderId);
		order.setOrderStatus(orderStatusDto.getOrderStatus());
		
		return orderRepository.save(order);
	}
	
	public void deleteAllAssociatedWithDrug(Long drugId) {
		List<Long> orderIds = drugQuantityRepository.findByDrugId(drugId).stream()
				.map(d -> d.getDrug().getId()).collect(Collectors.toList());
		
		List<Order> orders = findByIdIn(orderIds);
		
		orderRepository.deleteAll(orders);
	}
	
	public void deleteAllAssociatedWithUser(Long userId) {
		List<Order> orders = orderRepository.findByUserId(userId);
		
		orderRepository.deleteAll(orders);
	}
	
	public List<Order> findByIdIn(List<Long> orderIds) {
		return orderRepository.findByIdIn(orderIds);
	}

	public Order reorderOrder(Long orderId, OrderDto orderDto) throws GlobalException {
		Order order = findById(orderId);
		
		Set<DrugQuantity> drugQuantities = new HashSet<>();
		for (DrugQuantity quantity : order.getDrugQuantities()) {
			Drug drug = drugService.findById(quantity.getDrug().getId());
			
			DrugQuantity drugQuantity = new DrugQuantity(quantity.getQuantity(), drug);
			drugQuantities.add(drugQuantity);
		}
		
		User user = userService.findById(orderDto.getUserId());
		
		Set<OrderPrescription> orderPrescriptions = new HashSet<>();
		for (OrderPrescription prescription: order.getOrderPrescriptions()) {
			OrderPrescription orderPrescription = new OrderPrescription(prescription.getPrescriptionUrl());
			orderPrescriptions.add(orderPrescription);
		}
		
		Order reorder = Order.builder()
				.orderStatus(OrderStatus.PENDING)
				.deliveryAddress(user.getAddress())
				.deliveryPhone(user.getPhone())
				.drugQuantities(drugQuantities)
				.totalPrice(order.getTotalPrice())
				.user(user)
				.orderPrescriptions(orderPrescriptions)
				.build();
				
		return orderRepository.save(reorder);
	}

}
