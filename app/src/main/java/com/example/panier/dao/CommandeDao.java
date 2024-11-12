package com.example.panier.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.panier.entity.Commande;

import java.util.List;

@Dao
public interface CommandeDao {
    @Insert
    void insertCommande(Commande commande);

    @Query("SELECT * FROM commande")
    List<Commande> getAllCommandes();
}
