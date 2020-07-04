using FieraServicesWebAPITest.Models;
using Microsoft.EntityFrameworkCore;

namespace FieraServicesWebAPITest.Contexts
{
    public class UserContext : DbContext
    {
        public UserContext(DbContextOptions<UserContext> options) : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
    }
}
