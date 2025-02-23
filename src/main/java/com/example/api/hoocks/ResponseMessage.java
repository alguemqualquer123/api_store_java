package com.example.api.hoocks;

public class ResponseMessage {
  private String message;

  public ResponseMessage(String message) {
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
