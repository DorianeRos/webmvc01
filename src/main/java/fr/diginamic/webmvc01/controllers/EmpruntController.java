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

import fr.diginamic.webmvc01.entities.Client;
import fr.diginamic.webmvc01.entities.Emprunt;
import fr.diginamic.webmvc01.entities.Livre;
import fr.diginamic.webmvc01.repository.CrudClientRepo;
import fr.diginamic.webmvc01.repository.CrudEmpruntRepo;
import fr.diginamic.webmvc01.repository.CrudLivreRepo;

@Controller
@RequestMapping("/emprunt")
public class EmpruntController {

	@Autowired
	CrudEmpruntRepo er;

	@Autowired
	CrudClientRepo cr;

	@Autowired
	CrudLivreRepo lr;

	public EmpruntController() {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/emprunts")
	public String findall(Model model) {
		model.addAttribute("emprunts", (List<Emprunt>) er.findAll());
		model.addAttribute("titre", "Liste des emprunts");
		model.addAttribute("er", er);
		return "emprunts/ListeEmprunt";
	}

	@GetMapping("/addemprunt")
	public String addT(Model model) {
		List<Client> listesClients = (List<Client>) cr.findAll();
		List<Livre> listeLivres = (List<Livre>) lr.findAll();
		model.addAttribute("empruntForm", new Emprunt());
		model.addAttribute("titre", "Ajout emprunt");
		model.addAttribute("listeClients", listesClients);
		model.addAttribute("listeLivres", listeLivres);
		return "emprunts/addemprunt";
	}

	@PostMapping("/addemprunt")
	public String add(Model model, @Valid @ModelAttribute("empruntForm") Emprunt empruntForm) {

		er.save(empruntForm);
		empruntForm.getLivresE().forEach(l -> {
			l.getEmpruntLivres().add(empruntForm);
			lr.save(l);
		});
		return "redirect:/emprunt/emprunts";
	}

	@GetMapping("/update/{id}")
	public String updateT(@PathVariable("id") Integer pid, Model model) {
		List<Client> listesClients = (List<Client>) cr.findAll();
		List<Livre> listeLivres = (List<Livre>) lr.findAll();
		Emprunt emprunt = er.findById(pid).get();
		model.addAttribute("empruntForm", emprunt);
		model.addAttribute("titre", "Modifier Emprunt");
		model.addAttribute("listeClients", listesClients);
		model.addAttribute("listeLivres", listeLivres);
		return "emprunts/updateE";
	}

	@PostMapping("/updateE")
	public String update(@Valid @ModelAttribute("empruntForm") Emprunt empruntForm, BindingResult result) {
		// manageBindingResult(result);
		er.save(empruntForm);
		empruntForm.getLivresE().forEach(l -> {
			l.getEmpruntLivres().add(empruntForm);
			lr.save(l);
		});

		return "redirect:/emprunt/emprunts";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer pid) throws Exception {
		Optional<Emprunt> e = er.findById(pid);
		if (e.isEmpty()) {
			/**
			 * Je déclenche l'exception ClientError package fr.diginamic.Rest01.exceptions;
			 * Exception Fonctionnelle = métier
			 */
			throw (new Exception("Emprunt id :" + pid + " non trouvé !"));
		}
		er.deleteById(pid);
		return "redirect:/emprunt/emprunts";
	}

}
