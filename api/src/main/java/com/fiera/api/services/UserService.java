package com.fiera.api.services;

import java.util.ArrayList;
import java.util.List;

import com.fiera.api.dtos.UserDTO;
import com.fiera.api.models.User;
import com.fiera.api.repositories.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository _userRepository;

    UserService(UserRepository userRepository) {
        _userRepository = userRepository;
    }

    public List<UserDTO> getUsers()
    {
        List<User> users = new ArrayList<>();
        List<UserDTO> usersDTOs = new ArrayList<>();
        try {
            users = _userRepository.findAll();
            ModelMapper modelMapper = new ModelMapper();
            for (User user : users) {
                usersDTOs.add(modelMapper.map(user, UserDTO.class));
            }
        } catch (Exception e) {
        }
        return usersDTOs;
    }
}