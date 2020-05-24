using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using EjercicioCrud.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace EjercicioCrud.Pages.ListaCursos
{
    public class EditModel : PageModel
    {
        private readonly ApplicationDBContext _context;

        public EditModel(ApplicationDBContext context)
        {
            _context = context;
        }

        [BindProperty]
        public Curso Curso { get; set; }

        [TempData]
        public string Message { get; set; }

        public async Task OnGet(int id)
        {
            Curso = await _context.Cursos.FindAsync(id);
        }

        public async Task<IActionResult> OnPost()
        {
            if(ModelState.IsValid)
            {
                var dbCurso = await _context.Cursos.FindAsync(Curso.Id);
                dbCurso.NombreCurso = Curso.NombreCurso;
                dbCurso.Horas = Curso.Horas;
                dbCurso.Precio = Curso.Precio;

                await _context.SaveChangesAsync();
                Message = "Curso Editado Correctamente";
                return RedirectToPage("Index");
            }
            return RedirectToPage();
        }
    }
}