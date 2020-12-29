package com.ravi.assignment.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 2458599266952793348L;
  
  public PatientNotFoundException(String message) {
    super(message);
  }
}
