package com.fiera.api.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import com.fiera.api.dtos.UserDTO;
import com.fiera.api.exceptions.ErrorMessage;
import com.fiera.api.exceptions.CustomBadRequestException;
import com.fiera.api.exceptions.CustomNotFoundException;
import com.fiera.api.services.UserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
@Api(value = "Fiera API", description = "Users Controller")
public class UserController {
  private final UserService userService;

  public UserController(final UserService newUserService) {
    userService = newUserService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get all users", notes = "Retrieving all user", httpMethod = "GET", response = UserDTO[].class)
  @ApiResponses({ @ApiResponse(code = 200, message = "Success", response = UserDTO.class, responseContainer = "List"),
      @ApiResponse(code = 401, message = "Unathorized") })
  public ResponseEntity<List<UserDTO>> getUsers() {
    return ResponseEntity.ok(userService.getUsers());
  }

  @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Get user by Id", notes = "Retrieving an User by Id", httpMethod = "GET", response = UserDTO.class)
  @ApiResponses({ @ApiResponse(code = 200, message = "Success", response = UserDTO.class),
      @ApiResponse(code = 400, message = "BadRequest", response = ErrorMessage.class),
      @ApiResponse(code = 404, message = "Not found", response = ErrorMessage.class),
      @ApiResponse(code = 401, message = "Unathorized") })
  public ResponseEntity<UserDTO> getUserById(@ApiParam(required = true, value = "User Id") @PathVariable Long userId)
      throws CustomNotFoundException {
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Update User", notes = "Update an user", httpMethod = "PUT")
  @ApiResponses({ @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 400, message = "BadRequest", response = ErrorMessage.class),
      @ApiResponse(code = 404, message = "Not found", response = ErrorMessage.class),
      @ApiResponse(code = 401, message = "Unathorized") })
  public ResponseEntity<Object> updateUser(@ApiParam(required = true, value = "User Id") @PathVariable Long userId,
      @Valid @RequestBody UserDTO userDTO) throws CustomNotFoundException, CustomBadRequestException {
    return ResponseEntity.ok(userService.updateUser(userId, userDTO));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Add User", notes = "Add an user", httpMethod = "POST")
  @ApiResponses({ @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 400, message = "BadRequest", response = ErrorMessage.class),
      @ApiResponse(code = 401, message = "Unathorized") })
  public ResponseEntity<Object> addUser(@Valid @RequestBody UserDTO userDTO) throws CustomBadRequestException {
    Long id = userService.addUser(userDTO);
    userDTO.setUserId(id);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(id)
      .toUri();
    
    return ResponseEntity.created(location).build();
  }

  @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Delete User", notes = "Delete an user", httpMethod = "DELETE")
  @ApiResponses({ @ApiResponse(code = 200, message = "Success"),
      @ApiResponse(code = 404, message = "Not found", response = ErrorMessage.class),
      @ApiResponse(code = 401, message = "Unathorized") })
  public ResponseEntity<Object> deleteUser(@ApiParam(required = true, value = "User Id") @PathVariable Long userId)
      throws CustomNotFoundException {
    return ResponseEntity.ok(userService.deleteUser(userId));
  }
}