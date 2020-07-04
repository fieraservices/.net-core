package com.fiera.api.dtos;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    @Id 
    @GeneratedValue
    private Integer UserId;
    @NotNull(message = "Doc Number is required")
    private String DocNumber;
    @NotNull(message = "First Name is required")
    private String FirstName;
    @NotNull(message = "Last Name is required")
    private String LastName;
    private String Email;
    private String Phone;
    private String Address;
}