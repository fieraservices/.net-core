using AutoMapper;
using FieraServicesWebAPITest.DTOs;
using FieraServicesWebAPITest.Exceptions;
using FieraServicesWebAPITest.Models;
using FieraServicesWebAPITest.Repositories;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FieraServicesWebAPITest.Services
{
    public interface IUserService
    {
        Task<List<UserDTO>> GetUsers();
        Task<UserDTO> GetUser(int id);
        Task<int> InsertUser(UserDTO userDTO);
        Task<bool> UpdateUser(UserDTO userDTO);
        Task<bool> DeleteUser(int id);
        Task<bool> UserExists(int id);
    }

    public class UserService : IUserService
    {
        private readonly UserRepository _userRepository;
        private readonly IMapper _mapper;

        public UserService(UserRepository userRepository, IMapper mapper)
        {
            _userRepository = userRepository;
            _mapper = mapper;
        }

        public async Task<List<UserDTO>> GetUsers()
        {
            var users = await _userRepository.GetUsers();
            return _mapper.Map<List<UserDTO>>(users);
        }

        public async Task<UserDTO> GetUser(int id)
        {
            var user = await _userRepository.GetUserById(id);
            if (user == null)
                return null;
            return _mapper.Map<UserDTO>(user);
        }

        public async Task<int> InsertUser(UserDTO userDTO)
        {
            try
            {
                var user = _mapper.Map<User>(userDTO);
                return await _userRepository.InsertUser(user);
            }
            catch (CustomException)
            {
                return 0;
            }
        }

        public async Task<bool> UpdateUser(UserDTO userDTO)
        {
            try
            {
                var user = _mapper.Map<User>(userDTO);
                return await _userRepository.UpdateUser(user);
            }
            catch (CustomException)
            {
                return false;
            }
        }

        public async Task<bool> DeleteUser(int id)
        {
            try
            {
                return await _userRepository.DeleteUser(id);
            }
            catch (CustomException)
            {
                return false;
            }
        }

        public async Task<bool> UserExists(int id)
        {
            return await _userRepository.UserExists(id);
        }
    }
}
