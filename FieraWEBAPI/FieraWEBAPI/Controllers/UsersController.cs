using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using FieraWEBAPI.Context;
using FieraWEBAPI.Models;
using FieraWEBAPI.DTOs;
using Microsoft.AspNetCore.Authorization;
using FieraWEBAPI.Services;

namespace FieraServicesWebAPITest.Controllers
{
    [Route("api/users")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly UsersService _userService;

        public UsersController(UsersService userService)
        {
            _userService = userService;
        }

        /// <summary>
        /// Returns a List of all users
        /// </summary>
        /// <returns>List of all Users</returns>
        [HttpGet]
        [Authorize]
        public async Task<ActionResult<IEnumerable<UserDTO>>> GetUsers()
        {
            return Ok(await _userService.GetUsers());
        }

        /// <summary>
        /// Returns an user for a given id
        /// </summary>
        /// <param name="id">User Id</param>
        /// <returns>User for especific Id</returns>
        [HttpGet("{id}")]
        [Authorize]
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
        /// <returns>Empty 200 response</returns>
        [HttpPut("{id}")]
        [Authorize]
        public async Task<IActionResult> PutUser(int id, UserDTO userDTO)
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
        /// <returns>User created 201 code</returns>
        [HttpPost]
        [Authorize]
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
        /// <returns>Empty 200 response</returns>
        [HttpDelete("{id}")]
        [Authorize]
        public async Task<IActionResult> DeleteUser(int id)
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
