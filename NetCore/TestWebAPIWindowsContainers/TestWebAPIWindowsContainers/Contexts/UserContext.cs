using Microsoft.EntityFrameworkCore;
using TestWebAPIWindowsContainers.Models;

namespace TestWebAPIWindowsContainers.Contexts
{
    public class UserContext : DbContext
    {
        public UserContext(DbContextOptions<UserContext> options) : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
    }
}
