package com.fiera.api.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="Users")
public class User {
    @Id 
    @GeneratedValue
    private Long UserId;
    @NotNull(message = "Doc Number is required")
    private String DocNumber;
    @NotNull(message = "First Name is required")
    private String FirstName;
    @NotNull(message = "Last Name is required")
    private String LastName;
    private String Email;
    private String Phone;
    private String Address;
    private String SecretField;
}