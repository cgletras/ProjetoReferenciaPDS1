package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
