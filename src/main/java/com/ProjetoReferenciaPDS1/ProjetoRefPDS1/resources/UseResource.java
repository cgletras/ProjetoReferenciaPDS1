package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.User;

@RestController
@RequestMapping(value = "/users")
public class UseResource {
	
	@GetMapping
	public ResponseEntity<User> findAll() {
		User u = new User(1L, "Carlos", "cgletras@gmail.com", "32241500", "cg1234");
		return ResponseEntity.ok().body(u);
	}

}
