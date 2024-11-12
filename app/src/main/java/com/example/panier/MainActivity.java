package com.example.panier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panier.database.AppDatabase;
import com.example.panier.entity.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button buttonAddProduct;
    private Button buttonShowCart; // Nouveau bouton pour afficher le panier

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewProducts);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonShowCart = findViewById(R.id.buttonShowCart); // Initialisation du bouton "Afficher le panier"

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();

        // Set up button click listener for adding products
        buttonAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivityForResult(intent, 100);
        });

        // Set up button click listener for showing cart
        buttonShowCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    // Load products from the database
    private void loadProducts() {
        AppDatabase db = AppDatabase.getAppDatabase(this);
        List<Product> productList = db.productDao().getAllProducts();

        // Pass both productList and context to ProductAdapter
        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadProducts(); // Refresh the list after adding a new product
        }
    }
}

