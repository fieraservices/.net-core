using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using TestWebAPIWindowsContainers.Contexts;
using TestWebAPIWindowsContainers.DTO;
using TestWebAPIWindowsContainers.Models;

namespace TestWebAPIWindowsContainers.Controllers
{
    [Route("api/users")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly UserContext _context;

        public UsersController(UserContext context)
        {
            _context = context;
            // if no records in the database add one to always have at least one
            if(_context.Users.Count()==0)
            {
                _context.Users.Add(new User
                {
                    DocNumber = "123456789",
                    FirstName = "Luke",
                    LastName = "Skywalker",
                    Email = "jedi@email.com",
                    Phone = "987654321",
                    Address = "123 death star avenue"
                });
                _context.SaveChanges();
            }
        }

        /// <summary>
        /// GET: api/Users
        /// </summary>
        /// <returns>List of all Users</returns>
        [HttpGet]
        public async Task<ActionResult<IEnumerable<UserDTO>>> GetUsers()
        {
            return Ok(await _context.Users
                .Select(x=> UserToDTO(x))
                .ToListAsync());
        }

        /// <summary>
        /// GET: api/Users/5
        /// </summary>
        /// <param name="id">User Id</param>
        /// <returns>User for especific Id</returns>
        [HttpGet("{id}")]
        public async Task<ActionResult<UserDTO>> GetUser(int id)
        {
            var user = await _context.Users.FindAsync(id);

            if (user == null)
            {
                return NotFound();
            }

            return Ok(UserToDTO(user));
        }

        /// <summary>
        /// PUT: api/Users/5
        /// </summary>
        /// <param name="id">User Id</param>
        /// <param name="userDTO">User object to update</param>
        /// <returns>Empty 200 response</returns>
        [HttpPut("{id}")]
        public async Task<IActionResult> PutUser(int id, UserDTO userDTO)
        {
            if (id != userDTO.UserId)
            {
                return BadRequest();
            }

            var user = await _context.Users.FindAsync(id);

            if (user == null)
            {
                return NotFound();
            }

            // According to the HTTP specification, a PUT request requires
            // the client to send the entire updated entity, not just the changes.
            // To support partial updates, implements HTTP PATCH
            user.DocNumber = userDTO.DocNumber;
            user.FirstName = userDTO.FirstName;
            user.LastName = userDTO.LastName;
            user.Email = userDTO.Email;
            user.Phone = userDTO.Phone;
            user.Address = userDTO.Address;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException) when (!UserExists(id))
            {
                    return NotFound();
            }

            return Ok();
        }

        /// <summary>
        /// POST: api/Users
        /// </summary>
        /// <param name="userDTO">User to create</param>
        /// <returns>User created 201 code</returns>
        [HttpPost]
        public async Task<ActionResult<UserDTO>> PostUser(UserDTO userDTO)
        {
            var user = new User
            {
                DocNumber = userDTO.DocNumber,
                FirstName = userDTO.FirstName,
                LastName = userDTO.LastName,
                Email = userDTO.Email,
                Phone = userDTO.Phone,
                Address = userDTO.Address
            };

            _context.Users.Add(user);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetUser), new { id = user.UserId }, UserToDTO(user));
        }

        /// <summary>
        /// DELETE: api/Users/5
        /// </summary>
        /// <param name="id">User id</param>
        /// <returns>Empty 200 response</returns>
        [HttpDelete("{id}")]
        public async Task<ActionResult<User>> DeleteUser(int id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }

            _context.Users.Remove(user);
            await _context.SaveChangesAsync();

            return Ok();
        }

        /// <summary>
        /// check if an user exists in the database, for a given id
        /// </summary>
        /// <param name="id">User Id</param>
        /// <returns>Bool indicating if the user exists</returns>
        private bool UserExists(int id)
        {
            return _context.Users.Any(e => e.UserId == id);
        }

        /// <summary>
        /// Map User into User DTO object
        /// </summary>
        /// <param name="user"></param>
        /// <returns>UserDTO object</returns>
        private static UserDTO UserToDTO(User user) =>
            new UserDTO
            {
                UserId = user.UserId,
                DocNumber = user.DocNumber,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Email = user.Email,
                Phone = user.Phone,
                Address = user.Address
            };
    }
}
