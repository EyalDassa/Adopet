package com.example.adopet.Fragments;


import static android.content.Intent.getIntent;

import static com.example.adopet.Utilities.DbManager.sortPets;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.adopet.Activities.AddPetActivity;
import com.example.adopet.Interfaces.CategoryCallBack;
import com.example.adopet.Interfaces.UserDetailsCallBack;
import com.example.adopet.Models.Category;
import com.example.adopet.Models.User;
import com.example.adopet.R;
import com.example.adopet.Utilities.DbManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.search.SearchView;
import com.google.android.material.textview.MaterialTextView;


public class HomeFragment extends Fragment {

    private MaterialTextView home_LBL_hello;
    private MaterialTextView home_LBL_currCategory;
    private Button home_BTN_add;
    private CategoriesFragment fragmentCategories;
    private PetsFragment fragmentPets;
    private DbManager dbManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(view);
        dbManager = DbManager.getInstance();
        dbManager.sortCategories();
        sortPets();
        fragmentCategories = new CategoriesFragment();
        fragmentPets = new PetsFragment();
        fragmentCategories.setCategoryCallBack(showCategoryCallBack);

        getChildFragmentManager().beginTransaction()
                .add(R.id.home_LAY_categories, fragmentCategories)
                .add(R.id.home_LAY_pets, fragmentPets)
                .commit();
        home_LBL_hello.setText("Hello, " + User.getInstance().getName() + "!");

        home_BTN_add.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPetActivity.class);
            intent.putExtra("isEdit", false);
            startActivity(intent);
        });

        return view;
    }

    private void findViews (View view) {
        home_LBL_hello = view.findViewById(R.id.home_LBL_hello);
        home_LBL_currCategory = view.findViewById(R.id.home_LBL_currCategory);
        home_BTN_add = view.findViewById(R.id.home_BTN_add);

    }

    private CategoryCallBack showCategoryCallBack = new CategoryCallBack() {
        @Override
        public void showCategory(Category category) {
            fragmentPets.setCategory(category);
            home_LBL_currCategory.setText(category.getName());
        }
    };
}
