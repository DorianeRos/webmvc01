package fr.diginamic.webmvc01.controllers;


	import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.diginamic.webmvc01.entities.Livre;
import fr.diginamic.webmvc01.repository.CrudLivreRepo;

	@Controller
	@RequestMapping("/livre")
	public class LivreController {
		@Autowired
		CrudLivreRepo lr;
		public LivreController() {
		// TODO Auto-generated constructor stub
		}
		@GetMapping("/livres")
		public String findall(Model model) {
		model.addAttribute("livres", (List<Livre>) lr.findAll());
		model.addAttribute("titre","Liste des livres");
		return "livres/ListeLivres";
		}
		
		@GetMapping("/addlivre")
		public String addT(Model model) {
		model.addAttribute("livreForm", new Livre() );
		model.addAttribute("titre","Ajout livre");
		return "livres/addlivre";
		}
		
		@PostMapping("/addlivre")
		public String add(Model model,
				@Valid @ModelAttribute("livreForm") Livre livreForm)
		{
			lr.save(livreForm);
			return "redirect:/livre/livres";
		}
		
		@GetMapping("/update/{id}")
		public String updateT(@PathVariable("id") Integer pid, Model model) {
			Livre livre = lr.findById(pid).get();
			model.addAttribute("livreForm", livre);
			model.addAttribute("titre", "Update livre");
			return "livres/updateL";
		}

		@PostMapping("/updateL")
		public String update(@Valid @ModelAttribute("LivreForm") Livre livreForm, BindingResult result) {
			// manageBindingResult(result);
			lr.save(livreForm);
			return "redirect:/livre/livres";
		}
		
		
		
		
		@GetMapping("/delete/{id}")
		public String delete(@PathVariable("id") Integer pid) throws Exception {
		Optional<Livre> l = lr.findById(pid);
		if(l.isEmpty()) {
		/**
		* Je déclenche l'exception ClientError
		* package fr.diginamic.Rest01.exceptions;
		* Exception Fonctionnelle = métier
		*/
		throw( new Exception("Livre id :"+pid+" non trouvé !"));
		}
		lr.deleteById(pid);
		return "redirect:/livre/livres";
		}

}
