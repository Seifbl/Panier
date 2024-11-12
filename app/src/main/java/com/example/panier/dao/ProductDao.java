package com.example.panier.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.panier.entity.Panier;
import com.example.panier.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE panierId IS NOT NULL")
    List<Product> getProductsInCart();

    @Query("UPDATE products SET inCart = :isInCart WHERE id = :productId")
    void updateProductInCartStatus(int productId, boolean isInCart);
    @Query("DELETE FROM products WHERE panierId IS NOT NULL")
    void clearCart();
    @Query("UPDATE products SET panierId = NULL WHERE id = :productId")
    void removeFromCart(int productId);



}
