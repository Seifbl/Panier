package com.example.panier.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PanierWithProducts {
    @Embedded
    public Panier panier;

    @Relation(parentColumn = "id", entityColumn = "panierId")
    public List<Product> products;
}
