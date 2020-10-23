package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;


    //Récupérer la liste des produits
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }


    //Récupérer un produit par son Id
    @RequestMapping(value = "/ProduitsParId/{id}", method = RequestMethod.GET)
    public Product afficherUnProduit(@PathVariable int id) {
        return productDao.findById(id);
    }




    //ajouter un produit
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        if(product.getPrix() <= 0)
        {
            throw new ProduitGratuitException("Le prix de vente ne peut pas être négatif ni égal à 0.");
        }

        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // supprimer un produit
    @RequestMapping(value = "/Produits/{id}", method = RequestMethod.DELETE)
    public void supprimerProduit(@PathVariable int id) {
        productDao.delete(id);
    }

    // Mettre à jour un produit
    @PostMapping(value = "/ModifierProduit")
    public void updateProduit(@RequestBody Product product) {

        if(product.getPrix() <= 0)
        {
            throw new ProduitGratuitException("Le prix de vente ne peut pas être négatif ni égal à 0.");
        }
        productDao.save(product);
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }

    @GetMapping(value = "/AdminProduits")
    public String calculerMargeProduit(){

        String chaine = "";
        List<Product> produits = productDao.findAll();
        for (Product product: produits ) {
            int diffPrix = product.getPrix() - product.getPrixAchat();
            chaine = chaine + product.toString() + " : " + diffPrix + "<br>";
        }
        return chaine;
    }

    @GetMapping(value = "/TrierProduits")
    public List<Product> trierProduitsParOrdreAlphabetique(){
        List<Product> produits = productDao.findByOrderByNomAsc();
        return produits;
    }

}
