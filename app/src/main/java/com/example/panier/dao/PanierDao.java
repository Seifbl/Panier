package com.example.panier.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Transaction;

import com.example.panier.entity.Panier;
import com.example.panier.entity.PanierWithProducts;
import com.example.panier.entity.Product;

import java.util.List;

@Dao
public interface PanierDao {


    @Transaction
    @Query("SELECT * FROM paniers WHERE id = :panierId")
    PanierWithProducts getPanierWithProducts(int panierId);

    @Transaction
    @Query("SELECT * FROM paniers")
    List<PanierWithProducts> getAllPaniersWithProducts();

    @Query("SELECT id FROM paniers ORDER BY id DESC LIMIT 1")
    int getLastInsertedId();


    @Insert
    void insertPanier(Panier panier);

    @Query("SELECT * FROM paniers")
    List<Panier> getAllPaniers();


}
