using System.ComponentModel.DataAnnotations;

namespace FieraServicesWebAPITest.DTOs
{
    public class UserDTO
    {
        [Key]
        public int UserId { get; set; }
        [Required]
        public string DocNumber { get; set; }
        [Required]
        public string FirstName { get; set; }
        [Required]
        public string LastName { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }
        public string Address { get; set; }
    }
}
