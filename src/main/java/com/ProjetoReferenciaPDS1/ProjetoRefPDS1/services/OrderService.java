package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.dto.OrderDTO;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.dto.OrderItemDTO;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.Order;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.OrderItem;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.Product;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.User;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.enums.OrderStatus;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories.OrderItemRepository;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories.OrderRepository;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories.ProductRepository;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories.UserRepository;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private AuthService authService; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	public List<OrderDTO> findAll() {
		List<Order> list = repository.findAll();
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}
	
	public OrderDTO findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		Order entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		authService.validadeOwnOrderOrAdmin(entity);
		return new OrderDTO(entity);
	}
	
	public List<OrderDTO> findByCliente(){
		User cliente = authService.authenticated();
		List<Order> list = repository.findByClient(cliente);
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	@Transactional
	public List<OrderItemDTO> findItems(Long id) {
		Order order = repository.getOne(id);
		authService.validadeOwnOrderOrAdmin(order);
		Set<OrderItem> set = order.getItems();
		return set.stream().map(e -> new OrderItemDTO(e)).collect(Collectors.toList());
	}

	@Transactional
	public List<OrderDTO> findByClientId(Long clientId) {
		User client = userRepository.getOne(clientId);
		List<Order> list = repository.findByClient(client);
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	@Transactional
	public OrderDTO placeOrder(List<OrderItemDTO> dto) {

		User client = authService.authenticated();
		Order order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, client);
		
		for(OrderItemDTO itemDTO: dto) {
			Product product = productRepository.getOne(itemDTO.getProductId());
			OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), itemDTO.getPrice());
			order.getItems().add(item);
		}
		
		repository.save(order);
		orderItemRepository.saveAll(order.getItems());
		
		return new OrderDTO(order);
		
	}
}
