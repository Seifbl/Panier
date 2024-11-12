package com.example.panier.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.panier.dao.ProductDao;
import com.example.panier.dao.PanierDao;
import com.example.panier.dao.CommandeDao;
import com.example.panier.entity.Converters;
import com.example.panier.entity.Product;
import com.example.panier.entity.Panier;
import com.example.panier.entity.Commande;

@Database(entities = {Product.class, Panier.class, Commande.class}, version = 2, exportSchema = false)  // Changer la version à 2
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ProductDao productDao();
    public abstract PanierDao panierDao();
    public abstract CommandeDao commandeDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "room_test_db")
                    .fallbackToDestructiveMigration() // Pour recréer la base de données en cas de changement de schéma
                    .allowMainThreadQueries() // Envisagez de supprimer pour une utilisation en production
                    .build();
        }
        return instance;
    }
}
