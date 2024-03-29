package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.dto.UserDTO;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.entities.User;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.repositories.UserRepository;
import com.ProjetoReferenciaPDS1.ProjetoRefPDS1.resources.exceptions.FieldMessage;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserDTO> {
	
	@Autowired
	private HttpServletRequest request; 
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String, String> map =
			(Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Long uriId = Long.parseLong(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		User user = userRepository.findByEmail(dto.getEmail());
		
		if(user!=null && !user.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email already exists"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
