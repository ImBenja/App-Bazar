package com.bazar.app.error;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String msje) {
    super(msje);
  }
}
