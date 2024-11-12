package com.example.panier;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import com.example.panier.database.AppDatabase;
import com.example.panier.entity.Commande;
import com.example.panier.entity.CommandeProduct;
import com.example.panier.entity.Product;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.io.File;
import java.util.Date;
import java.util.List;


public class CartActivity extends AppCompatActivity {

    private LinearLayout linearLayoutCartItems;
    private TextView textViewTotalPrice;
    private Button buttonOrderNow;
    private AppDatabase db;
    private PaymentSheet paymentSheet;
    private String clientSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize Stripe Payment Configuration with your publishable key
        PaymentConfiguration.init(this, "pk_test_51MhKLdGY9S3rG4mjFxHzo5t4FTlVJR4V7bHw3FUJYIbatKMpfjyyT3NbLvBxkNl4AzuV2SWIRACjTmFUA8nbP0Xy006kUj1Fut");

        db = AppDatabase.getAppDatabase(this);

        linearLayoutCartItems = findViewById(R.id.linearLayoutCartItems);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        buttonOrderNow = findViewById(R.id.buttonOrderNow);

        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        buttonOrderNow.setOnClickListener(v -> initiatePayment());

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

            if (product.getImageUrl() != null && product.getImageUrl().startsWith("file://")) {
                File imgFile = new File(Uri.parse(product.getImageUrl()).getPath());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageViewProduct.setImageBitmap(myBitmap);
                } else {
                    imageViewProduct.setVisibility(View.GONE);
                }
            } else {
                imageViewProduct.setVisibility(View.GONE);
            }

            buttonRemoveProduct.setOnClickListener(v -> {
                db.productDao().removeFromCart(product.getId());
                cartItems.remove(product);
                linearLayoutCartItems.removeView(productView);
                updateTotalPrice(cartItems);
                Toast.makeText(this, product.getName() + " removed from cart", Toast.LENGTH_SHORT).show();
            });

            linearLayoutCartItems.addView(productView);
            total += product.getPrice();
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

    private void initiatePayment() {
        List<Product> cartItems = db.productDao().getProductsInCart();
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        fetchPaymentIntentClientSecret();
    }

    private void fetchPaymentIntentClientSecret() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        PaymentIntentRequest request = new PaymentIntentRequest(1000); // Example amount in cents

        Log.d("CartActivity", "Initiating payment intent fetch..."); // Log start of method

        apiService.createPaymentIntent(request).enqueue(new Callback<PaymentIntentResponse>() {
            @Override
            public void onResponse(Call<PaymentIntentResponse> call, Response<PaymentIntentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    clientSecret = response.body().getClientSecret();
                    Log.d("CartActivity", "Client secret received: " + clientSecret);
                    presentPaymentSheet();
                } else {
                    Log.e("CartActivity", "Failed to fetch PaymentIntent - response unsuccessful");
                    Toast.makeText(CartActivity.this, "Failed to fetch PaymentIntent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentIntentResponse> call, Throwable t) {
                Log.e("CartActivity", "Error fetching PaymentIntent", t); // Log the error
                Toast.makeText(CartActivity.this, "Failed to fetch PaymentIntent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void presentPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Your Company Name")
                .allowsDelayedPaymentMethods(true)
                .build();

        paymentSheet.presentWithPaymentIntent(clientSecret, configuration);
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
            createOrder();
        } else {
            Toast.makeText(this, "Payment canceled or failed", Toast.LENGTH_SHORT).show();
        }
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
        loadCartItems();
    }
}
