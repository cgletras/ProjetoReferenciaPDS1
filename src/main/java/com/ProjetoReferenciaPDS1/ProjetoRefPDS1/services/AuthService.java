package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.dto.CredentialsDTO;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.dto.TokenDTO;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.Order;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.User;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories.UserRepository;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.security.JWTUtil;

import services.exceptions.JWTAuthenticationException;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	UserRepository userRepository;

	@Transactional(readOnly = true)
	public TokenDTO authenticate(CredentialsDTO dto) {
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(),
					dto.getPassword());
			authenticationManager.authenticate(authToken);
			String token = jwtUtil.generateToken(dto.getEmail());
			return new TokenDTO(dto.getEmail(), token);
		} catch (AuthenticationException e) {
			throw new JWTAuthenticationException("Bad credentials");
		}
	}

	public User authenticated() {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			return userRepository.findByEmail(userDetails.getUsername());
		} catch (Exception e) {
			throw new JWTAuthenticationException("Access denied");
		}
	}
	
	public void validadeSelfOrAdmin(Long userId) {
		User user = authenticated();
		if (user == null || (!user.getId().equals(userId) && !user.hasRole("ROLE_ADMIN"))) {
			throw new JWTAuthenticationException("Access denied");
		}
	}
	
	public void validadeOwnOrderOrAdmin(Order order) {
		User user = authenticated();
		if (user == null || (!user.getId().equals(order.getClient().getId()) && !user.hasRole("ROLE_ADMIN"))) {
			throw new JWTAuthenticationException("Access denied");
		}
	}
	
	public TokenDTO refreshTolen() {
		User user = authenticated();
		return new TokenDTO(user.getEmail(), jwtUtil.generateToken(user.getEmail()));
	}
}
