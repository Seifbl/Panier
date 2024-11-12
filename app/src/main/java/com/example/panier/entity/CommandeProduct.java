package com.example.panier.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "commande_product")
public class CommandeProduct {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int commandeId;
    private int productId;

    public CommandeProduct(int commandeId, int productId) {
        this.commandeId = commandeId;
        this.productId = productId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCommandeId() { return commandeId; }
    public void setCommandeId(int commandeId) { this.commandeId = commandeId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
}
