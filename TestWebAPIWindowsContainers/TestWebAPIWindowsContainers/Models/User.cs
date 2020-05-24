namespace TestWebAPIWindowsContainers.Models
{
    /// <summary>
    /// Class to hold User on Database
    /// </summary>
    public class User
    {
        public int UserId { get; set; }
        public string DocNumber { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string Phone { get; set; }
        public string Address { get; set; }
        public string Secret { get; set; }
    }
}
