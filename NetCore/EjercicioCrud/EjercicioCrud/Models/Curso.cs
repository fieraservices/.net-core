using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace EjercicioCrud.Models
{
    public class Curso
    {
        [Key]
        public int Id { get; set; }
        [Required]
        [Display(Name = "Nombre del Curso")]
        public string NombreCurso { get; set; }
        [Display(Name = "Duracion del Curso (Horas)")]
        public int Horas { get; set; }
        [Display(Name = "Precio del Curso (Dolares)")]
        public int Precio { get; set; }
    }
}
