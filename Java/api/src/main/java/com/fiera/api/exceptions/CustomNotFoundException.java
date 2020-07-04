package com.fiera.api.exceptions;

public class CustomNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public CustomNotFoundException() {
    super("Record not found");
  }

  public CustomNotFoundException(String message) {
    super(message);
  }
}