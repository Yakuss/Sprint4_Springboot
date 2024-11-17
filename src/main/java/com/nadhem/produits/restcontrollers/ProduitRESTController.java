package com.nadhem.produits.restcontrollers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nadhem.produits.entities.Produit;
import com.nadhem.produits.service.ProduitService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProduitRESTController {
	@Autowired
	ProduitService produitService;
	
	@GetMapping("all")
	public List<Produit> getAllProduits() {
		return produitService.getAllProduits();
	 } 		
	
	@GetMapping("/getbyid/{id}")
	//@GetMapping("/getbyid/{id}")
	public Produit getProduitById(@PathVariable("id") Long id) {	
		return produitService.getProduit(id);
    }
	
	@PostMapping("/addprod")
	//@PostMapping("/addprod")
	public Produit createProduit(@RequestBody Produit produit) {
		return produitService.saveProduit(produit);
	}

	@PutMapping("/updateprod")
	//@PutMapping("/updateprod")
	public Produit updateProduit(@RequestBody Produit produit) {
		return produitService.updateProduit(produit);
	}

	@DeleteMapping("/delprod/{id}")
	//@DeleteMapping("/delprod/{id}")
	public void deleteProduit(@PathVariable("id") Long id)
	{
		produitService.deleteProduitById(id);
	}
	
	@GetMapping("/prodscat/{idCat}")
	public List<Produit> getProduitsByCatId(@PathVariable("idCat") Long idCat) {
		return produitService.findByCategorieIdCat(idCat);
	 }
	
	@GetMapping("/auth")
	Authentication getAuth(Authentication auth)
	{
	return auth;
	}


}
