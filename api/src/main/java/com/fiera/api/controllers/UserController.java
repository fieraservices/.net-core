package com.fiera.api.controllers;

import java.net.HttpURLConnection;
import java.util.List;

import com.fiera.api.dtos.UserDTO;
import com.fiera.api.services.UserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping
@PreAuthorize("isAuthenticated()")
@Api(value = "Fiera API", description = "Users Controller")
public class UserController {
    private final UserService _userService;

    UserController(UserService userService) {
        _userService = userService;
    }


    @GetMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all users", nickname = "getUsers", httpMethod = "GET")
    @ApiResponses(value = {@ApiResponse(code = HttpURLConnection.HTTP_OK,
            message = "Successfully retrieved info",  response = UserDTO.class, responseContainer = "List")})
    public ResponseEntity<List<UserDTO>> getCountries(){
      return ResponseEntity.ok(_userService.getUsers());
    }
}