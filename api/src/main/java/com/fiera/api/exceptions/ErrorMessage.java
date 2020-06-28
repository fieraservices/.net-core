package com.fiera.api.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorMessage {

  private String id;
  private String level;
  private LocalDateTime timestamp;
  private String message;
  private String details;

  /**
   * Create ErrorMessage with just a message String.
   *
   * @param message description of error.
   */
  public ErrorMessage(String message) {
    this.message = message;
  }

  /**
   * Create ErrorMessage with a message String and the details of the error
   *
   * @param message description of error
   * @param details name of the Field in java class.
   */
  public ErrorMessage(String message, String details) {
    this.message = message;
    this.details = details;
  }

  /**
   * Create ErrorMessage with a message String, details of the error and timestamp of the occurrence
   *
   * @param message   description of error
   * @param details   name of the Field in java class.
   * @param timestamp context id to identify the record if list of records are validated
   */
  public ErrorMessage(String message, String details, LocalDateTime timestamp) {
    this(message, details);
    this.timestamp = timestamp;
  }

  public static ErrorMessage of(String message) {
    return new ErrorMessage(message);
  }

  public static ErrorMessage of(String message, String details) {
    return new ErrorMessage(message, details);
  }

  public static ErrorMessage of(String message, LocalDateTime timestamp) {
    return new ErrorMessage(message, null, timestamp);
  }

  public static ErrorMessage of(String message, String details, LocalDateTime timestamp) {
    return new ErrorMessage(message, details, timestamp);
  }

}
