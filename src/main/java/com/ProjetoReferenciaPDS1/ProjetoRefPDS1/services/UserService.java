package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.dto.UserDTO;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.User;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories.UserRepository;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.resources.exceptions.DatabaseException;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public List<UserDTO> findAll() {
		List<User> list = repository.findAll();
		return list.stream().map(e -> new UserDTO(e)).collect(Collectors.toList());
	}
	
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new UserDTO(entity);
	}
	
	public User insert(User obj) {
		return repository.save(obj);
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public User update(Long id, User obj) {
		try {
		User entity = repository.getOne(id);
		updateData(entity, obj);
		return repository.save(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(User entity, User obj) {
		entity.setName(obj.getName());
		entity.setEmail(obj.getEmail());
		entity.setPhone(obj.getPhone());
	}
}
