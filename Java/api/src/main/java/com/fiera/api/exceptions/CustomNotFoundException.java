package com.fiera.api.exceptions;

public class CustomNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  public CustomNotFoundException(String message) {
    super(message);
  }
}