using AutoMapper;
using FieraServicesWebAPITest.DTOs;
using FieraServicesWebAPITest.Models;

namespace FieraServicesWebAPITest.Mapper
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            CreateMap<User, UserDTO>().ReverseMap();
        }
    }
}
