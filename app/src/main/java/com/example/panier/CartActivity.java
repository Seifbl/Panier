package com.example.panier;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.panier.database.AppDatabase;
import com.example.panier.entity.Product;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView textViewTotalPrice; // TextView for displaying total price

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize RecyclerView and TextView for total price
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        loadCartItems();
    }

    private void loadCartItems() {
        AppDatabase db = AppDatabase.getAppDatabase(this);

        // Load products in the cart based on `panierId`
        List<Product> cartItems = db.productDao().getProductsInCart();

        // Set adapter with the products in the cart
        cartAdapter = new CartAdapter(cartItems, this);
        recyclerViewCart.setAdapter(cartAdapter);

        // Calculate and display the total price
        double totalPrice = 0.0;
        for (Product product : cartItems) {
            totalPrice += product.getPrice();
        }
        textViewTotalPrice.setText(String.format("Total: $%.2f", totalPrice));
    }
}
