package com.example.panier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.panier.database.AppDatabase;
import com.example.panier.entity.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextName, editTextTitle, editTextSubtitle, editTextPrice;
    private Button buttonChooseImage, buttonSaveProduct;
    private ImageView imageViewSelected;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextSubtitle = findViewById(R.id.editTextSubtitle);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonSaveProduct = findViewById(R.id.buttonSaveProduct);

        // Button to choose image from gallery
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Button to save product
        buttonSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                // Créez un nom de fichier unique pour chaque image
                String fileName = "product_image_" + System.currentTimeMillis() + ".jpg";
                File file = new File(getFilesDir(), fileName);

                // Copier l'image sélectionnée vers le fichier de stockage interne
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                OutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // Fermer les flux
                inputStream.close();
                outputStream.close();

                // Afficher l'image dans l'ImageView et mettre à jour `imageUri`
                imageViewSelected.setImageURI(Uri.fromFile(file));
                imageUri = Uri.fromFile(file); // Stockez le chemin de fichier pour la base de données

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur de chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }





        private void saveProduct() {
        String name = editTextName.getText().toString().trim();
        String title = editTextTitle.getText().toString().trim();
        String subtitle = editTextSubtitle.getText().toString().trim();
        String priceText = editTextPrice.getText().toString().trim();

        if (name.isEmpty() || title.isEmpty() || priceText.isEmpty() || imageUri == null) {
            Toast.makeText(AddProductActivity.this, "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(AddProductActivity.this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Créer le produit sans panierId
        Product product = new Product(name, imageUri.toString(), title, subtitle, price);

        // Insérer le produit dans la base de données
        AppDatabase db = AppDatabase.getAppDatabase(AddProductActivity.this);
        db.productDao().insertProduct(product);

        Toast.makeText(AddProductActivity.this, "Product added", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, new Intent());
        finish();
    }


}
