using AutoMapper;
using FieraWEBAPI.DTOs;
using FieraWEBAPI.Exceptions;
using FieraWEBAPI.Models;
using FieraWEBAPI.Repositories;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace FieraWEBAPI.Services
{
    public class UsersService
    {
        private readonly UsersRepository _usersRepository;
        private readonly IMapper _mapper;

        public UsersService(UsersRepository usersRepository, IMapper mapper)
        {
            _usersRepository = usersRepository;
            _mapper = mapper;
        }

        public async Task<List<UserDTO>> GetUsers()
        {
            var users = await _usersRepository.GetUsers();
            return _mapper.Map<List<UserDTO>>(users);
        }

        public async Task<UserDTO> GetUser(int id)
        {
            var user = await _usersRepository.GetUserById(id);
            if (user == null)
                return null;
            return _mapper.Map<UserDTO>(user);
        }

        public async Task<int> InsertUser(UserDTO userDTO)
        {
            try
            {
                var user = _mapper.Map<User>(userDTO);
                return await _usersRepository.InsertUser(user);
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
                return await _usersRepository.UpdateUser(user);
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
                return await _usersRepository.DeleteUser(id);
            }
            catch (CustomException)
            {
                return false;
            }
        }

        public async Task<bool> UserExists(int id)
        {
            return await _usersRepository.UserExists(id);
        }
    }
}
