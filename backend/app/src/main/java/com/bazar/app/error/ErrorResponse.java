package com.bazar.app.error;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
  private int status;
  private String message;
  private LocalDateTime timestamp;
  private List<String> details;

  public ErrorResponse(int status, String message, LocalDateTime timestamp) {
    this.status = status;
    this.message = message;
    this.timestamp = timestamp;
  }
}