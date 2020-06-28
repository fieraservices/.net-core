using NUnit.Framework;
using Microsoft.Data.Sqlite;
using Microsoft.EntityFrameworkCore;
using FieraServicesWebAPITest.Contexts;
using System.Threading.Tasks;
using Newtonsoft.Json;
using FieraServicesWebAPITest.DTOs;
using FieraServicesWebAPITest.Services;
using FieraServicesWebAPITest.Repositories;
using AutoMapper;
using FieraServicesWebAPITest.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http;
using Moq;
using FluentAssertions.Execution;
using FluentAssertions;

namespace FieraServicesWebAPITest.Controllers.Tests
{
    [TestFixture()]
    public class UsersControllerTests
    {
        private UsersController _usersController;
        private UserRepository _userRepository;
        private IUserService _userService;
        private IMapper _mapper;

        [SetUp]
        public void SetUp()
        {
            var connection = new SqliteConnection("DataSource=:memory:");
            connection.Open();

            var options = new DbContextOptionsBuilder<UserContext>()
                .UseSqlite(connection)
                .Options;

            var context = new UserContext(options);

            context.Database.EnsureCreated();

            _userRepository = new UserRepository(context);

            var cfg = new MapperConfiguration(opts =>
            {
                opts.CreateMap<User, UserDTO>().ReverseMap();
            });

            _mapper = cfg.CreateMapper();

            _userService = new UserService(_userRepository, _mapper);
            _usersController = new UsersController(_userService);
        }

        [Test()]
        public async Task GetUsers_ReturnsOK()
        {
            var response = await _usersController.GetUsers();
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull();
                response.Result.Should().NotBeNull().And.BeOfType<OkObjectResult>();
                var okResponse = (OkObjectResult)response.Result;
                okResponse.Should().NotBeNull().And.BeOfType<OkObjectResult>();
                okResponse.StatusCode.Should().NotBeNull().And.Be(StatusCodes.Status200OK);
            }
        }

        [Test()]
        public async Task GetUser_ValidId_ReturnsOk()
        {
            var response = await _usersController.GetUser(1);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull();
                response.Result.Should().NotBeNull().And.BeOfType<OkObjectResult>();
                var okResponse = (OkObjectResult)response.Result;
                okResponse.Should().NotBeNull().And.BeOfType<OkObjectResult>();
                okResponse.StatusCode.Should().NotBeNull().And.Be(StatusCodes.Status200OK);
            }
        }

        [Test()]
        public async Task GetUser_InvalidId_ReturnsNotFound()
        {
            var response = await _usersController.GetUser(0);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull();
                response.Result.Should().NotBeNull().And.BeOfType<NotFoundResult>();
                var notFoundResponse = (NotFoundResult)response.Result;
                notFoundResponse.Should().NotBeNull().And.BeOfType<NotFoundResult>();
                notFoundResponse.StatusCode.Should().Be(StatusCodes.Status404NotFound);
            }
        }

        [Test()]
        public async Task PutUser_ValidIdAndObject_ReturnsOK()
        {
            var user = new UserDTO
            {
                UserId = 1,
                DocNumber = "123456789",
                FirstName = "Luke",
                LastName = "Skywalker",
                Email = "jedi@email.com",
                Phone = "987654321",
                Address = "123 death star avenue"
            };

            var response = await  _usersController.PutUser(1, user);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull().And.BeOfType<OkResult>();
                var okResponse = (OkResult)response;
                okResponse.Should().NotBeNull().And.BeOfType<OkResult>();
                okResponse.StatusCode.Should().Be(StatusCodes.Status200OK);
            }
        }

        [Test()]
        public async Task PutUser_InvalidRequest_ReturnsBadRequest()
        {
            var user = new UserDTO
            {
                UserId = 0,
            };

            var response = await _usersController.PutUser(1, user);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull().And.BeOfType<BadRequestResult>();
                var badRequestResponse = (BadRequestResult)response;
                badRequestResponse.Should().NotBeNull().And.BeOfType<BadRequestResult>();
                badRequestResponse.StatusCode.Should().Be(StatusCodes.Status400BadRequest);
            }
        }

        [Test()]
        public async Task PutUser_InvalidId_ReturnsNotFound()
        {
            var user = new UserDTO
            {
                UserId = 0,
            };

            var response = await _usersController.PutUser(0, user);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull().And.BeOfType<NotFoundResult>();
                var notFoundResponse = (NotFoundResult)response;
                notFoundResponse.Should().NotBeNull().And.BeOfType<NotFoundResult>();
                notFoundResponse.StatusCode.Should().Be(StatusCodes.Status404NotFound);
            }
        }

        [Test()]
        public async Task PutUser_ValidIdAndInvalidObject_ReturnsConflict()
        {
            var user = new UserDTO
            {
                UserId = 1,
            };

            var response = await _usersController.PutUser(1, user);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull().And.BeOfType<ConflictResult>();
                var conflictResponse = (ConflictResult)response;
                conflictResponse.Should().NotBeNull().And.BeOfType<ConflictResult>();
                conflictResponse.StatusCode.Should().Be(StatusCodes.Status409Conflict);
            }
        }

        [Test()]
        public async Task PostUser_ValidObject_ReturnsCreatedAt()
        {
            var user = new UserDTO
            {
                DocNumber = "13243546576",
                FirstName = "Leia",
                LastName = "Organa",
                Email = "leia@email.com",
                Phone = "978675645",
                Address = "213 alderaan street "
            };

            var response = await _usersController.PostUser(user);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull();
                response.Result.Should().NotBeNull().And.BeOfType<CreatedAtActionResult>();
                var createdAtResponse = (CreatedAtActionResult)response.Result;
                createdAtResponse.Should().NotBeNull().And.BeOfType<CreatedAtActionResult>();
                createdAtResponse.StatusCode.Should().NotBeNull().And.Be(StatusCodes.Status201Created);
            }
        }

        [Test()]
        public async Task PostUser_InvalidObject_ReturnsConflict()
        {
            var user = new UserDTO
            {
            };

            var response = await _usersController.PostUser(user);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull();
                response.Result.Should().NotBeNull().And.BeOfType<ConflictResult>();
                var conflictResponse = (ConflictResult)response.Result;
                conflictResponse.Should().NotBeNull().And.BeOfType<ConflictResult>();
                conflictResponse.StatusCode.Should().Be(StatusCodes.Status409Conflict);
            }
        }

        [Test()]
        public async Task DeleteUser_ValidId_ReturnsOK()
        {
            var response = await _usersController.DeleteUser(1);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull().And.BeOfType<OkResult>();
                var okResponse = (OkResult)response;
                okResponse.Should().NotBeNull().And.BeOfType<OkResult>();
                okResponse.StatusCode.Should().Be(StatusCodes.Status200OK);
            }
        }

        [Test()]
        public async Task DeleteUser_InvalidId_ReturnsNotFound()
        {
            var response = await _usersController.DeleteUser(0);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull().And.BeOfType<NotFoundResult>();
                var notFoundResponse = (NotFoundResult)response;
                notFoundResponse.Should().NotBeNull().And.BeOfType<NotFoundResult>();
                notFoundResponse.StatusCode.Should().Be(StatusCodes.Status404NotFound);
            }
        }


        [Test()]
        public async Task DeleteUser_Exception_ReturnsConflict()
        {
            var mockService = new Mock<IUserService>();
            mockService.Setup(p => p.UserExists(1)).ReturnsAsync(true);
            mockService.Setup(p => p.DeleteUser(1)).ReturnsAsync(false);
            var mockController = new UsersController(mockService.Object);
            var response = await mockController.DeleteUser(1);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            using (new AssertionScope())
            {
                response.Should().NotBeNull().And.BeOfType<ConflictResult>();
                var conflictResponse = (ConflictResult)response;
                conflictResponse.Should().NotBeNull().And.BeOfType<ConflictResult>();
                conflictResponse.StatusCode.Should().Be(StatusCodes.Status409Conflict);
            }
        }
    }
}