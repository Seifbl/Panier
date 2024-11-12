package com.example.panier.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "paniers")
public class Panier {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
