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

namespace FieraServicesWebAPITest.Controllers.Tests
{
    [TestFixture()]
    public class UsersControllerTests
    {
        private UsersController _usersController;
        private UserService _userService;
        private UserRepository _userRepository;
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
            Assert.IsNotNull(response);
            Assert.IsNotNull(response.Result);
            Assert.IsInstanceOf<OkObjectResult>(response.Result);
            var okResponse = (OkObjectResult)response.Result;
            Assert.IsNotNull(okResponse);
            Assert.True(okResponse is OkObjectResult);
            Assert.AreEqual(StatusCodes.Status200OK, okResponse.StatusCode);
        }

        [Test()]
        public async Task GetUser_ValidId_ReturnsOk()
        {
            var response = await _usersController.GetUser(1);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            Assert.IsNotNull(response);
            Assert.IsNotNull(response.Result);
            Assert.IsInstanceOf<OkObjectResult>(response.Result);
            var okResponse = (OkObjectResult)response.Result;
            Assert.IsNotNull(okResponse);
            Assert.True(okResponse is OkObjectResult);
            Assert.AreEqual(StatusCodes.Status200OK, okResponse.StatusCode);
        }

        [Test()]
        public async Task GetUser_InvalidId_ReturnsNotFound()
        {
            var response = await _usersController.GetUser(0);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            Assert.IsNotNull(response);
            Assert.IsNotNull(response.Result);
            Assert.IsInstanceOf<NotFoundResult>(response.Result);
            var notFoundResponse = (NotFoundResult)response.Result;
            Assert.IsNotNull(notFoundResponse);
            Assert.True(notFoundResponse is NotFoundResult);
            Assert.AreEqual(StatusCodes.Status404NotFound, notFoundResponse.StatusCode);
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

            Assert.IsNotNull(response);
            Assert.IsInstanceOf<OkResult>(response);
            var okResponse = (OkResult)response;
            Assert.IsNotNull(okResponse);
            Assert.True(okResponse is OkResult);
            Assert.AreEqual(StatusCodes.Status200OK, okResponse.StatusCode);
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

            Assert.IsNotNull(response);
            Assert.IsInstanceOf<BadRequestResult>(response);
            var badRequestResponse = (BadRequestResult)response;
            Assert.IsNotNull(badRequestResponse);
            Assert.True(badRequestResponse is BadRequestResult);
            Assert.AreEqual(StatusCodes.Status400BadRequest, badRequestResponse.StatusCode);
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

            Assert.IsNotNull(response);
            Assert.IsInstanceOf<NotFoundResult>(response);
            var notFoundResponse = (NotFoundResult)response;
            Assert.IsNotNull(notFoundResponse);
            Assert.True(notFoundResponse is NotFoundResult);
            Assert.AreEqual(StatusCodes.Status404NotFound, notFoundResponse.StatusCode);
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

            Assert.IsNotNull(response);
            Assert.IsInstanceOf<ConflictResult>(response);
            var conflictResponse = (ConflictResult)response;
            Assert.IsNotNull(conflictResponse);
            Assert.True(conflictResponse is ConflictResult);
            Assert.AreEqual(StatusCodes.Status409Conflict, conflictResponse.StatusCode);
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
            Assert.IsNotNull(response);
            Assert.IsNotNull(response.Result);
            Assert.IsInstanceOf<CreatedAtActionResult>(response.Result);
            var createdAtResponse = (CreatedAtActionResult)response.Result;
            Assert.IsNotNull(createdAtResponse);
            Assert.True(createdAtResponse is CreatedAtActionResult);
            Assert.AreEqual(StatusCodes.Status201Created, createdAtResponse.StatusCode);
        }

        [Test()]
        public async Task PostUser_InvalidObject_ReturnsConflict()
        {
            var user = new UserDTO
            {
            };

            var response = await _usersController.PostUser(user);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));
            Assert.IsNotNull(response);
            Assert.IsNotNull(response.Result);
            Assert.IsInstanceOf<ConflictResult>(response.Result);
            var conflictResponse = (ConflictResult)response.Result;
            Assert.IsNotNull(conflictResponse);
            Assert.True(conflictResponse is ConflictResult);
            Assert.AreEqual(StatusCodes.Status409Conflict, conflictResponse.StatusCode);
        }

        [Test()]
        public async Task DeleteUser_ValidId_ReturnsOK()
        {
            var response = await _usersController.DeleteUser(1);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));

            Assert.IsNotNull(response);
            Assert.IsInstanceOf<OkResult>(response);
            var okResponse = (OkResult)response;
            Assert.IsNotNull(okResponse);
            Assert.True(okResponse is OkResult);
            Assert.AreEqual(StatusCodes.Status200OK, okResponse.StatusCode);
        }

        [Test()]
        public async Task DeleteUser_InvalidId_ReturnsNotFound()
        {
            var response = await _usersController.DeleteUser(0);
            TestContext.WriteLine(JsonConvert.SerializeObject(response));

            Assert.IsNotNull(response);
            Assert.IsInstanceOf<NotFoundResult>(response);
            var notFoundResponse = (NotFoundResult)response;
            Assert.IsNotNull(notFoundResponse);
            Assert.True(notFoundResponse is NotFoundResult);
            Assert.AreEqual(StatusCodes.Status404NotFound, notFoundResponse.StatusCode);
        }
    }
}