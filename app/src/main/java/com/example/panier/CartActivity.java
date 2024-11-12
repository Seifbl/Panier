package com.example.panier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.panier.database.AppDatabase;
import com.example.panier.entity.Commande;
import com.example.panier.entity.CommandeProduct;
import com.example.panier.entity.Product;

import java.io.File;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private LinearLayout linearLayoutCartItems;
    private TextView textViewTotalPrice;
    private Button buttonOrderNow;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = AppDatabase.getAppDatabase(this);

        linearLayoutCartItems = findViewById(R.id.linearLayoutCartItems);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        buttonOrderNow = findViewById(R.id.buttonOrderNow);

        buttonOrderNow.setOnClickListener(v -> createOrder());

        loadCartItems();
    }

    private void loadCartItems() {
        List<Product> cartItems = db.productDao().getProductsInCart();
        linearLayoutCartItems.removeAllViews();
        double total = 0.0;

        for (Product product : cartItems) {
            View productView = LayoutInflater.from(this).inflate(R.layout.item_cart, linearLayoutCartItems, false);

            TextView textViewName = productView.findViewById(R.id.textViewName);
            TextView textViewPrice = productView.findViewById(R.id.textViewPrice);
            ImageView imageViewProduct = productView.findViewById(R.id.imageViewProduct);
            Button buttonRemoveProduct = productView.findViewById(R.id.buttonRemoveProduct);

            textViewName.setText(product.getName());
            textViewPrice.setText(String.format("$%.2f", product.getPrice()));

            // Load the image if the file path exists
            if (product.getImageUrl() != null && product.getImageUrl().startsWith("file://")) {
                File imgFile = new File(Uri.parse(product.getImageUrl()).getPath());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageViewProduct.setImageBitmap(myBitmap);
                } else {
                    imageViewProduct.setVisibility(View.GONE); // Hide if the image doesn't exist
                }
            } else {
                imageViewProduct.setVisibility(View.GONE); // Hide if no valid path
            }

            // Remove product button logic
            buttonRemoveProduct.setOnClickListener(v -> {
                db.productDao().removeFromCart(product.getId());
                cartItems.remove(product); // Remove from cart items list
                linearLayoutCartItems.removeView(productView); // Remove view
                updateTotalPrice(cartItems); // Update total
                Toast.makeText(this, product.getName() + " removed from cart", Toast.LENGTH_SHORT).show();
            });

            linearLayoutCartItems.addView(productView);
            total += product.getPrice(); // Calculate total
        }

        textViewTotalPrice.setText(String.format("Total: $%.2f", total));
    }

    private void updateTotalPrice(List<Product> cartItems) {
        double total = 0.0;
        for (Product product : cartItems) {
            total += product.getPrice();
        }
        textViewTotalPrice.setText(String.format("Total: $%.2f", total));
    }

    private void createOrder() {
        List<Product> cartItems = db.productDao().getProductsInCart();
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Commande newCommande = new Commande(new Date());
        db.commandeDao().insertCommande(newCommande);
        int commandeId = newCommande.getId();

        for (Product product : cartItems) {
            CommandeProduct commandeProduct = new CommandeProduct(commandeId, product.getId());
            db.commandeProductDao().insertCommandeProduct(commandeProduct);
        }

        db.productDao().clearCart();
        Toast.makeText(this, "Order created successfully!", Toast.LENGTH_SHORT).show();
        loadCartItems(); // Refresh cart after order
    }
}
