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
import fr.diginamic.webmvc01.repository.CrudClientRepo;
import fr.diginamic.webmvc01.repository.CrudEmpruntRepo;
import fr.diginamic.webmvc01.repository.CrudLivreRepo;

@Controller
@RequestMapping("/client")
public class ClientsController {
	@Autowired
	CrudClientRepo cr;

	@Autowired
	CrudEmpruntRepo er;

	@Autowired
	CrudLivreRepo lr;

	public ClientsController() {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/clients")
	public String findall(Model model) {
		model.addAttribute("clients", (List<Client>) cr.findAll());
		model.addAttribute("titre", "Liste des clients");
		return "clients/Liste";
	}

	@GetMapping("/add")
	public String addT(Model model) {
		model.addAttribute("clientForm", new Client());
		model.addAttribute("titre", "Ajout client");
		return "clients/add";
	}

	@PostMapping("/add")
	public String add(Model model, @Valid @ModelAttribute("clientForm") Client clientForm) {
		cr.save(clientForm);
		return "redirect:/client/clients";
	}

	@GetMapping("/update/{id}")
	public String updateT(@PathVariable("id") Integer pid, Model model) {
		Client client = cr.findById(pid).get();
		model.addAttribute("clientForm", client);
		model.addAttribute("titre", "Update client");
		return "clients/updateC";
	}
	
	@PostMapping("/updateC")
	public String update(@Valid @ModelAttribute("clientForm") Client clientForm, BindingResult result) {
		// manageBindingResult(result);
		cr.save(clientForm);
		return "redirect:/client/clients";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer pid) throws Exception {
		Optional<Client> c = cr.findById(pid);

		if (c.isEmpty()) {

			/**
			 * Je d??clenche l'exception ClientError package fr.diginamic.Rest01.exceptions;
			 * Exception Fonctionnelle = m??tier
			 */
			throw (new Exception("Client id :" + pid + " non trouv?? !"));
		}

		cr.getEmpruntByClient(pid).forEach(e -> {
			er.findByLivre(e).forEach(l -> {
				l.getEmpruntLivres().remove(e);
				lr.save(l);
			});
			// suppression des liens avec compo
			er.delete(e);

		});
		cr.deleteById(pid);
		return "redirect:/client/clients";
	}

}
