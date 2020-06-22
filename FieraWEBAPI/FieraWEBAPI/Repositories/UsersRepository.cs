using FieraWEBAPI.Context;
using FieraWEBAPI.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using FieraWEBAPI.Exceptions;

namespace FieraWEBAPI.Repositories
{
    public class UsersRepository
    {
        private readonly UserContext _context;

        public UsersRepository(UserContext context)
        {
            _context = context;
            // if no records in the database add one to always have at least one
            if (_context.Users.Count() == 0)
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

        public async Task<List<User>> GetUsers()
        {
            return await _context.Users.ToListAsync();
        }

        public async Task<User> GetUserById(int id)
        {
            return await _context.Users.FindAsync(id);
        }

        public async Task<int> InsertUser(User user)
        {
            try
            {
                await _context.Users.AddAsync(user);
                await _context.SaveChangesAsync();
                return user.UserId;
            }
            catch (DbUpdateConcurrencyException)
            {
                throw new CustomException("Custom Exception Message 1");
            }
            catch (DbUpdateException)
            {
                throw new CustomException("Custom Exception Message 2");
            }
        }

        public async Task<bool> UpdateUser(User user)
        {
            try
            {

                _context.Users.Update(user);
                await _context.SaveChangesAsync();
                return true;
            }
            catch (DbUpdateConcurrencyException)
            {
                throw new CustomException("Custom Exception Message 1");
            }
            catch (DbUpdateException)
            {
                throw new CustomException("Custom Exception Message 2");
            }
        }

        public async Task<bool> DeleteUser(int id)
        {
            try
            {
                User user = await _context.Users.FindAsync(id);
                _context.Users.Remove(user);
                await _context.SaveChangesAsync();
                return true;
            }
            catch (DbUpdateConcurrencyException)
            {
                throw new CustomException("Custom Exception Message 1");
            }
            catch (DbUpdateException)
            {
                throw new CustomException("Custom Exception Message 2");
            }
        }

        public async Task<bool> UserExists(int id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user != null)
            {
                _context.Entry(user).State = EntityState.Detached;
                return true;
            }
            else
                return false;
        }
    }
}
