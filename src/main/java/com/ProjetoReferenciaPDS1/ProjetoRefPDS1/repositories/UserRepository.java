package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
