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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Api
public class ProductController {

    @Autowired
    private ProductDao productDao;


    //Récupérer la liste des produits
    @ApiOperation(value = "Products", notes = "Gets all products")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
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
    @ApiOperation(value = "Product", notes = "Gets a product by his id")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
    @RequestMapping(value = "/ProduitsParId/{id}", method = RequestMethod.GET)
    public Product afficherUnProduit(@PathVariable int id) {
        return productDao.findById(id);
    }




    //ajouter un produit
    @ApiOperation(value = "Add product", notes = "Adds a product with method POST")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
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
    @ApiOperation(value = "Delete product", notes = "Deletes a product by his id with method DELETE")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
    @RequestMapping(value = "/Produits/{id}", method = RequestMethod.DELETE)
    public void supprimerProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }

    // Mettre à jour un produit
    @ApiOperation(value = "Update product", notes = "Updates a product with method POST")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
    @PostMapping(value = "/ModifierProduit")
    public void updateProduit(@RequestBody Product product) {

        if(product.getPrix() <= 0)
        {
            throw new ProduitGratuitException("Le prix de vente ne peut pas être négatif ni égal à 0.");
        }
        productDao.save(product);
    }


    //Pour les tests
    @ApiOperation(value = "Get expensive products", notes = "Gets expensive products")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }

    @ApiOperation(value = "Get products and margin", notes = "Gets products with their margin")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
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

    @ApiOperation(value = "Get sorted products", notes = "Gets products that are sorted by name")
    @ApiResponses(
            value = {@ApiResponse(code = 200, message = "Success"),
                    @ApiResponse(code = 400, message = "Bad request"),
                    @ApiResponse(code = 401, message = "Unauthorized"),
                    @ApiResponse(code = 403, message = "Forbidden access"),
                    @ApiResponse(code = 404, message = "Students not found"),
                    @ApiResponse(code = 500, message = "Server error")}
    )
    @GetMapping(value = "/TrierProduits")
    public List<Product> trierProduitsParOrdreAlphabetique(){
        List<Product> produits = productDao.findByOrderByNomAsc();
        return produits;
    }

}
