package com.fiera.api.services;

import java.util.ArrayList;
import java.util.List;

import com.fiera.api.dtos.UserDTO;
import com.fiera.api.exceptions.CustomBadRequestException;
import com.fiera.api.exceptions.CustomNotFoundException;
import com.fiera.api.models.User;
import com.fiera.api.repositories.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository newUserRepository, ModelMapper newModelMapper) {
        userRepository = newUserRepository;
        modelMapper = newModelMapper;
    }

    public List<UserDTO> getUsers() {
        List<UserDTO> usersDTOs = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            usersDTOs.add(modelMapper.map(user, UserDTO.class));
        }
        return usersDTOs;
    }

    public UserDTO getUserById(Long id) throws CustomNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("User not found for this id :: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    public boolean updateUser(Long id, UserDTO userDTO) throws CustomBadRequestException, CustomNotFoundException {
        if (id != userDTO.getUserId()) {
            throw new CustomBadRequestException("Not matching Ids");
        }
        boolean userExists = userRepository.existsById(id);
        if (userExists) {
            User user = modelMapper.map(userDTO, User.class);
            userRepository.save(user);
            return true;
        } else {
            throw new CustomNotFoundException("User not found for this id :: " + id);
        }
    }

    public Long addUser(UserDTO userDTO) throws CustomBadRequestException {
        if (userDTO.getUserId() != null) {
            throw new CustomBadRequestException("User must have no Id");
        }
        User savedUser = userRepository.save(modelMapper.map(userDTO, User.class));
        return savedUser.getUserId();
    }

    public boolean deleteUser(Long id) throws CustomNotFoundException {
        boolean userExists = userRepository.existsById(id);
        if (userExists) {
            userRepository.deleteById(id);
            return true;
        } else {
            throw new CustomNotFoundException("User not found for this id :: " + id);
        }
    }
}