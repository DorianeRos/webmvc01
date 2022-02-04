package fr.diginamic.webmvc01.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.diginamic.webmvc01.entities.Emprunt;
import fr.diginamic.webmvc01.entities.Livre;


public interface CrudEmpruntRepo extends CrudRepository<Emprunt, Integer> {
	/**
	 * Me retourne la liste des livres d'un emprunt.
	 * @param emp
	 * @return
	 */
	@Query("select l from Livre l where :emp MEMBER OF l.empruntLivres")
    public Iterable<Livre> findByLivre(Emprunt emp);


}
