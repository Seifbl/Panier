package com.example.panier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.panier.database.AppDatabase;
import com.example.panier.entity.Panier;
import com.example.panier.entity.Product;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Afficher le nom et le prix du produit
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("Price: $" + product.getPrice());

        // Charger l'image à partir du chemin de fichier local
        if (product.getImageUrl() != null && product.getImageUrl().startsWith("file://")) {
            File imgFile = new File(Uri.parse(product.getImageUrl()).getPath()); // Convertir URI en chemin de fichier

            if (imgFile.exists()) {
                // Décoder et afficher l'image
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageViewProduct.setImageBitmap(myBitmap);
            } else {
                holder.imageViewProduct.setVisibility(View.GONE); // Masquer si le fichier n'existe pas
            }
        } else {
            holder.imageViewProduct.setVisibility(View.GONE); // Masquer si l'URI est incorrect
        }

        holder.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product);
                Toast.makeText(context, product.getName() + " ajouté au panier", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void addToCart(Product product) {
        AppDatabase db = AppDatabase.getAppDatabase(context);

        // Vérifier si un panier existe déjà
        List<Panier> paniers = db.panierDao().getAllPaniers();
        Panier panier;

        if (paniers.isEmpty()) {
            // Aucun panier existant, en créer un nouveau
            panier = new Panier();
            db.panierDao().insertPanier(panier);
            panier.setId(db.panierDao().getLastInsertedId()); // Utilisez une méthode pour obtenir l'ID du dernier panier inséré
        } else {
            // Utiliser le premier panier existant
            panier = paniers.get(0);
        }

        // Associer le produit au panier en mettant à jour le `panierId`
        product.setPanierId(panier.getId());
        db.productDao().updateProduct(product); // Assurez-vous d'avoir `updateProduct` dans ProductDao
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewProduct;
        Button buttonAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            buttonAddToCart = itemView.findViewById(R.id.buttonAddToCart);
        }
    }
}
