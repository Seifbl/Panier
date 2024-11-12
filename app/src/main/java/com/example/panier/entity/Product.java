package com.example.panier.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String imageUrl;
    private String title;
    private String subtitle;
    private double price;
    // Nouveau champ pour savoir si le produit est dans le panier
    private boolean inCart;

    // Getters et Setters pour le champ inCart
    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }
    // Champ panierId optionnel (nullable)
    private Integer panierId;

    // Constructeur principal utilisé par Room
    public Product(String name, String imageUrl, String title, String subtitle, double price, Integer panierId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.panierId = panierId;
    }

    // Constructeur sans panierId, annoté avec @Ignore pour être utilisé uniquement en code et non par Room
    @Ignore
    public Product(String name, String imageUrl, String title, String subtitle, double price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.panierId = null; // Pas de panier associé par défaut
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public double getPrice() {
        return price;
    }

    public Integer getPanierId() {
        return panierId;
    }

    public void setPanierId(Integer panierId) {
        this.panierId = panierId;
    }
}
