using AutoMapper;
using FieraWEBAPI.DTOs;
using FieraWEBAPI.Models;

namespace FieraWEBAPI.Mapper
{
    public class MappingsProfile : Profile
    {
        public MappingsProfile()
        {
            CreateMap<User, UserDTO>().ReverseMap();
        }
    }
}