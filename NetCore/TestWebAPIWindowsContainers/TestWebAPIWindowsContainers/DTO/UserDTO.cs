namespace TestWebAPIWindowsContainers.DTO
{
    /// <summary>
    /// Class to expose data
    /// </summary>
    public class UserDTO
    {
        public int UserId { get; set; }
        public string DocNumber { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }
        public string Address { get; set; }
    }
}
