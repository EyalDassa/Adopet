package com.example.adopet.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopet.Models.Category;
import com.example.adopet.Utilities.ImageLoader;
import com.example.adopet.Interfaces.CategoryCallBack;
import com.example.adopet.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private ArrayList<Category> categories;
    private CategoryCallBack categoryCallBack;

    public CategoryAdapter(ArrayList<Category> categories) {this.categories = categories;}


    public void setCategoryCallBack(CategoryCallBack categoryCallBack) { this.categoryCallBack = categoryCallBack; }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = getItem(position);
        Log.d("Adapterr", "Binding category: " + category.getName() + ", " + category.getImg());
        ImageLoader.getInstance().load(category.getImg(),holder.categories_CARD_img);
        holder.categories_CARD_title.setText(category.getName());
    }

    @Override
    public int getItemCount() {return categories == null ? 0 : categories.size();}

    public Category getItem(int position){
        return categories.get(position);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView categories_CARD_title;
        private ShapeableImageView categories_CARD_img;

        public CategoryViewHolder(@NonNull View view) {
            super(view);
            categories_CARD_title = view.findViewById(R.id.categories_CARD_title);
            categories_CARD_img = view.findViewById(R.id.categories_CARD_img);
            categories_CARD_img.setOnClickListener(v -> {
                categoryCallBack.showCategory(categories.get(getAdapterPosition()));
            });
        }
    }
}

