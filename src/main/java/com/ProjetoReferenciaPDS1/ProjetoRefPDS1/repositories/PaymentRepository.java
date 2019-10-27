package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
