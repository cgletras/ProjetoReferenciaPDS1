package com.ProjetoReferenciaPDS1.ProjetoRefPDS1.resources.exceptions;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseException(String msg) {
		super(msg);
	}
}
