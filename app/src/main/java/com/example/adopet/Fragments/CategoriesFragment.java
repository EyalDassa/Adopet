package com.example.adopet.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adopet.Adapters.CategoryAdapter;
import com.example.adopet.Models.Category;
import com.example.adopet.Utilities.DbManager;
import com.example.adopet.Interfaces.CategoryCallBack;
import com.example.adopet.R;

public class CategoriesFragment extends Fragment {

    private RecyclerView categories_LST_categories;
    private CategoryCallBack categoryCallBack;
    private static CategoryAdapter categoryAdapter;

    public static CategoryAdapter getCategoryAdapter() {
        return categoryAdapter;
    }

    public void setCategoryCallBack(CategoryCallBack callBack) {
        this.categoryCallBack = callBack;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);


        categories_LST_categories = view.findViewById(R.id.categories_LST_categories);

        DbManager dbManager = DbManager.getInstance();
        categoryAdapter = new CategoryAdapter(dbManager.getCategories());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        categories_LST_categories.setLayoutManager(linearLayoutManager);
        categories_LST_categories.setAdapter(categoryAdapter);
        categoryAdapter.setCategoryCallBack(category -> {
            if (categoryCallBack != null)
                categoryCallBack.showCategory(category);
        });


        return view;
    }

}