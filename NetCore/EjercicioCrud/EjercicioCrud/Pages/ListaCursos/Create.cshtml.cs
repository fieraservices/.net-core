using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using EjercicioCrud.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace EjercicioCrud.Pages.ListaCursos
{
    public class CreateModel : PageModel
    {
        private readonly ApplicationDBContext _context;

        public CreateModel(ApplicationDBContext context)
        {
            _context = context;
        }

        [BindProperty]
        public Curso Curso { get; set; }

        [TempData]
        public string Message { get; set; }

        public void OnGet()
        {

        }

        public async Task<IActionResult> OnPost()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }

            _context.Add(Curso);
            await _context.SaveChangesAsync();
            Message = "Curso creado correctamente";
            return RedirectToPage("Index");
        }
    }
}