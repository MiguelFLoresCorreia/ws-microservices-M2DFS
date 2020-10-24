package com.ecommerce.microcommerce.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
//@JsonFilter("monFiltreDynamique")
public class Product {

    @Id
    @GeneratedValue
    @ApiModelProperty(
            value = "L'id du produit", name = "id", dataType = "int", example = "0", required = true
    )
    private int id;

    @Length(min=3, max=20, message = "Nom trop long ou trop court. Et oui messages sont plus stylés que ceux de Spring")
    @ApiModelProperty(
            value = "Le nom du produit", name = "nom", dataType = "String", example = "Stylo", required = true
    )
    private String nom;

    @Min(value = 1)
    @ApiModelProperty(
            value = "Le prix du produit", name = "prix", dataType = "int", example = "500", required = true
    )
    private int prix;

    //information que nous ne souhaitons pas exposer
    @ApiModelProperty(
            value = "Le prix d'achat du produit", name = "prixAchat", dataType = "int", example = "200", required = true
    )
    private int prixAchat;

    //constructeur par défaut
    public Product() {
    }

    //constructeur pour nos tests
    public Product(int id, String nom, int prix, int prixAchat) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.prixAchat = prixAchat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(int prixAchat) {
        this.prixAchat = prixAchat;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                '}';
    }
}
