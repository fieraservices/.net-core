using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Threading.Tasks;
using EjercicioCrud.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;

namespace EjercicioCrud.Pages.ListaCursos
{
    public class IndexModel : PageModel
    {
        private readonly ApplicationDBContext _context;

        public IndexModel(ApplicationDBContext context)
        {
            _context = context;
        }

        public IEnumerable<Curso> Cursos { get; set; }

        [TempData]
        public string Message { get; set; }

        public async Task OnGet()
        {
            Cursos = await  _context.Cursos.ToListAsync();
        }

        public async Task<IActionResult> OnPostDelete(int id)
        {
            var curso = await _context.Cursos.FindAsync(id);
            if (curso==null)
            {
                return NotFound();
            }
            _context.Cursos.Remove(curso);
            await _context.SaveChangesAsync();
            Message = "Curso Borrado Correctamente";
            return RedirectToPage("Index");
        }
    }
}