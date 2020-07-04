using FieraWEBAPI.Models;
using Microsoft.EntityFrameworkCore;

namespace FieraWEBAPI.Context
{
    public class UserContext : DbContext
    {
        public UserContext(DbContextOptions<UserContext> options) : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
    }
}
