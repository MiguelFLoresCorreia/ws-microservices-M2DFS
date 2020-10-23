package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class ProductControllerTest {
    @Mock
    ProductDao productDao;
    @InjectMocks
    ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAfficherUnProduit() {
        when(productDao.findById(anyInt())).thenReturn(new Product(0, "nom", 0, 0));

        Product result = productController.afficherUnProduit(0);
        Product testProduct = new Product(0, "nom", 0, 0);

        Assertions.assertEquals(testProduct.getId(), result.getId());
        Assertions.assertEquals(testProduct.getNom(), result.getNom());
        Assertions.assertEquals(testProduct.getPrix(), result.getPrix());
        Assertions.assertEquals(testProduct.getPrixAchat(), result.getPrixAchat());
    }

    @Test
    void testAjouterProduit() throws ProduitGratuitException {
        ResponseEntity<Void> result = productController.ajouterProduit(new Product(0, null, 100, 50));
        Assertions.assertTrue(result.getStatusCodeValue() >= 200);
        Assertions.assertTrue( result.getStatusCodeValue() < 300);
    }

    @Test
    void testExceptionAjouterProduit() throws ProduitGratuitException{
        Assertions.assertThrows(ProduitGratuitException.class, () -> {
            productController.ajouterProduit(new Product(0, null, 0, 50));
        });

    }

    @Test
    void testSupprimerProduit() {
        productController.supprimerProduit(0);
    }

    @Test
    void testUpdateProduit() {
        productController.updateProduit(new Product(0, "prenom", 100, 0));
    }

    @Test
    void testCalculerMargeProduit() {
        when(productDao.findAll()).thenReturn(Arrays.<Product>asList(new Product(0, "nom", 100, 60)));

        String result = productController.calculerMargeProduit();
        Assertions.assertEquals("Product{id=0, nom='nom', prix=100} : 40<br>", result);
    }

    @Test
    void testTrierProduitsParOrdreAlphabetique() {
        when(productDao.findByOrderByNomAsc()).thenReturn(Arrays.<Product>asList(new Product(0, "prenom", 100, 0), new Product(1, "nom", 50, 0)));

        List<Product> result = productController.trierProduitsParOrdreAlphabetique();
        Product produit1 = new Product(0, "prenom", 100, 0);
        Product produit2 = new Product(1, "nom", 50, 0);

        Assertions.assertEquals(produit1.getId(), result.get(0).getId());
        Assertions.assertEquals(produit1.getNom(), result.get(0).getNom());
        Assertions.assertEquals(produit1.getPrix(), result.get(0).getPrix());
        Assertions.assertEquals(produit1.getPrixAchat(), result.get(0).getPrixAchat());
        Assertions.assertEquals(produit2.getId(), result.get(1).getId());
        Assertions.assertEquals(produit2.getNom(), result.get(1).getNom());
        Assertions.assertEquals(produit2.getPrix(), result.get(1).getPrix());
        Assertions.assertEquals(produit2.getPrixAchat(), result.get(1).getPrixAchat());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme