package com.fiera.api.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class ErrorMessage {

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();
  private String message;
  private String details;

}
