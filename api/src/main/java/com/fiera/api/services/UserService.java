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

    private final UserRepository _userRepository;
    private final ModelMapper _modelMapper;

    UserService(UserRepository userRepository, ModelMapper modelMapper) {
        _userRepository = userRepository;
        _modelMapper = modelMapper;
    }

    public List<UserDTO> getUsers() {
        List<UserDTO> usersDTOs = new ArrayList<>();
        List<User> users = _userRepository.findAll();
        for (User user : users) {
            usersDTOs.add(_modelMapper.map(user, UserDTO.class));
        }
        return usersDTOs;
    }

    public UserDTO getUserById(int id) throws CustomNotFoundException {
        User user = _userRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("User not found for this id :: " + id));
        return _modelMapper.map(user, UserDTO.class);
    }

    public boolean updateUser(int id, UserDTO userDTO) throws CustomBadRequestException, CustomNotFoundException {
        if (id != userDTO.getUserId()) {
            throw new CustomBadRequestException("Not matching Ids");
        }
        boolean userExists = _userRepository.existsById(id);
        if (userExists) {
            User user = _modelMapper.map(userDTO, User.class);
            _userRepository.save(user);
            return true;
        } else {
            throw new CustomNotFoundException("User not found for this id :: " + id);
        }
    }

    public int addUser(UserDTO userDTO) throws CustomBadRequestException {
        if (userDTO.getUserId() != null) {
            throw new CustomBadRequestException("User must have no Id");
        }
        User user = _modelMapper.map(userDTO, User.class);
        _userRepository.save(user);
        return user.getUserId();
    }

    public boolean deleteUser(int id) throws CustomNotFoundException {
        boolean userExists = _userRepository.existsById(id);
        if (userExists) {
            _userRepository.deleteById(id);
            return true;
        } else {
            throw new CustomNotFoundException("User not found for this id :: " + id);
        }
    }
}