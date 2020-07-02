package com.fiera.api.exceptions;

public class CustomBadRequestException  extends Exception {

    private static final long serialVersionUID = 1L;
  
    public CustomBadRequestException() {
      super("Bad Request");
    }
  
    public CustomBadRequestException(String message) {
      super(message);
    }
  }