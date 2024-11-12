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
import com.example.panier.entity.Product;

import java.io.File;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartItems;
    private Context context;

    public CartAdapter(List<Product> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);

        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText("Price: $" + product.getPrice());

        if (product.getImageUrl() != null && product.getImageUrl().startsWith("file://")) {
            File imgFile = new File(Uri.parse(product.getImageUrl()).getPath());

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageViewProduct.setImageBitmap(myBitmap);
            } else {
                holder.imageViewProduct.setVisibility(View.GONE);
            }
        } else {
            holder.imageViewProduct.setVisibility(View.GONE);
        }

        // Configuration du bouton supprimer
        holder.buttonRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProductFromCart(product, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // Méthode pour supprimer un produit du panier sans le supprimer de la base de données
    private void removeProductFromCart(Product product, int position) {
        AppDatabase db = AppDatabase.getAppDatabase(context);

        // Mettre à jour le statut `inCart` du produit
        db.productDao().updateProductInCartStatus(product.getId(), false);

        // Supprimer le produit de la liste du panier et mettre à jour la vue
        cartItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItems.size());

        Toast.makeText(context, product.getName() + " a été retiré du panier", Toast.LENGTH_SHORT).show();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewProduct;
        Button buttonRemoveProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            buttonRemoveProduct = itemView.findViewById(R.id.buttonRemoveProduct);
        }
    }
}
