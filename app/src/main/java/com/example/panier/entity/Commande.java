package com.example.panier.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;



import java.util.Date;
import java.util.List;

@Entity(tableName = "commande")
@TypeConverters(Converters.class)
public class Commande {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private List<Product> products;

    private Date date;

    public Commande(List<Product> products, Date date) {
        this.products = products;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    // Setter for id to allow Room to set it
    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
