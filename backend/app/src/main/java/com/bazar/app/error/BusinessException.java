package com.bazar.app.error;

public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }

}
