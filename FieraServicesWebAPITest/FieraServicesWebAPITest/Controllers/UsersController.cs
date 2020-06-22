using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using FieraServicesWebAPITest.DTOs;
using Microsoft.AspNetCore.Authorization;
using FieraServicesWebAPITest.Services;
using Microsoft.AspNetCore.Http;
using System.Net.Mime;

namespace FieraServicesWebAPITest.Controllers
{
    [Route("api/users")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly UserService _userService;

        public UsersController(UserService userService)
        {
            _userService = userService;
        }

        /// <summary>
        /// Returns a List of all users
        /// </summary>
        /// <returns>List of all Users</returns>
        /// <response code="200">Returns a List of all users</response>
        [HttpGet]
        [Authorize]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public async Task<ActionResult<IEnumerable<UserDTO>>> GetUsers()
        {
            return Ok(await _userService.GetUsers());
        }

        /// <summary>
        /// Returns an user for a given id
        /// </summary>
        /// <param name="id">User Id</param>
        /// <returns>User for especific Id</returns>
        /// <response code="200">Returns an user for a given id</response>
        /// <response code="404">If the user is not found</response>
        [HttpGet("{id}")]
        [Authorize]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        public async Task<ActionResult<UserDTO>> GetUser(int id)
        {
            var userDTO = await _userService.GetUser(id);

            if (userDTO == null)
            {
                return NotFound();
            }

            return Ok(userDTO);
        }

        /// <summary>
        /// update an user for a given id
        /// </summary>
        /// <param name="id">User Id</param>
        /// <param name="userDTO">User object to update</param>
        /// <returns>Empty Ok response</returns>
        /// <response code="200">Empty Ok response</response>
        /// <response code="400">If the id is different to the userId</response>
        /// <response code="404">If the user is not found</response>
        /// <response code="409">If there is a problem updating the user</response>
        [HttpPut("{id}")]
        [Authorize]
        [Consumes(MediaTypeNames.Application.Json)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        public async Task<ActionResult> PutUser(int id, UserDTO userDTO)
        {
            // According to the HTTP specification, a PUT request requires
            // the client to send the entire updated entity, not just the changes.
            // To support partial updates, implements HTTP PATCH

            if (id != userDTO.UserId)
            {
                return BadRequest();
            }

            var userExists = await _userService.UserExists(id);

            if (!userExists)
            {
                return NotFound();
            }

            var status = await _userService.UpdateUser(userDTO);
            if (!status)
            {
                return Conflict();
            }

            return Ok();
        }

        /// <summary>
        /// add a new user
        /// </summary>
        /// <param name="userDTO">User to create</param>
        /// <returns>A newly created user</returns>
        /// <response code="201">Returns the newly created user</response>
        /// <response code="409">If there is a problem updating the user</response> 
        [HttpPost]
        [Authorize]
        [Consumes(MediaTypeNames.Application.Json)]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        public async Task<ActionResult<UserDTO>> PostUser(UserDTO userDTO)
        {
            var id = await _userService.InsertUser(userDTO);
            if (id == 0)
            {
                return Conflict();
            }

            return CreatedAtAction(nameof(GetUser), new { id }, userDTO);
        }

        /// <summary>
        /// delete an user for a given id
        /// </summary>
        /// <param name="id">User id</param>
        /// <returns>Empty Ok response</returns>
        /// <response code="200">Empty Ok response</response>
        /// <response code="404">If the user is not found</response>
        /// <response code="409">If there is a problem updating the user</response>
        [HttpDelete("{id}")]
        [Authorize]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status409Conflict)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        public async Task<ActionResult> DeleteUser(int id)
        {
            var userExists = await _userService.UserExists(id);

            if (!userExists)
            {
                return NotFound();
            }

            var status = await _userService.DeleteUser(id);
            if (!status)
            {
                return Conflict();
            }

            return Ok();
        }

    }
}
