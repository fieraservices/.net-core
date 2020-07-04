using AutoMapper;
using FieraServicesWebAPITest.DTOs;
using FieraServicesWebAPITest.Models;
using System.Diagnostics.CodeAnalysis;

namespace FieraServicesWebAPITest.Mapper
{
    [ExcludeFromCodeCoverage]
    public class MappingProfile : Profile
    {
        
        public MappingProfile()
        {
            CreateMap<User, UserDTO>().ReverseMap();
        }
    }
}
