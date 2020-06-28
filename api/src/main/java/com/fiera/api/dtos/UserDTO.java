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
    @NotNull
    private String DocNumber;
    @NotNull
    private String FirstName;
    @NotNull
    private String LastName;
    private String Email;
    private String Phone;
    private String Address;
}