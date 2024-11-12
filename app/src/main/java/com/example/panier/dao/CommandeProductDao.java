package com.example.panier.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.panier.entity.CommandeProduct;

import java.util.List;

@Dao
public interface CommandeProductDao {
    @Insert
    void insertCommandeProduct(CommandeProduct commandeProduct);

    @Query("SELECT * FROM commande_product WHERE commandeId = :commandeId")
    List<CommandeProduct> getProductsForCommande(int commandeId);
}
