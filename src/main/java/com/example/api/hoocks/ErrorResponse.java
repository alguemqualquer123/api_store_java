package com.example.api.hoocks;

public class ErrorResponse {
  private String message;

  public ErrorResponse(String message) {
      this.message = message;
  }

  // Getter
  public String getMessage() {
      return message;
  }

  // Setter
  public void setMessage(String message) {
      this.message = message;
  }
}
