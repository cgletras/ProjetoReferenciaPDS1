package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.Order;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByClient(User cliente);
}
