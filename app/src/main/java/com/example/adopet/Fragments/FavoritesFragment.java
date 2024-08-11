package com.example.adopet.Fragments;

import static com.example.adopet.Utilities.DbManager.getPetById;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopet.Activities.PetDetailsActivity;
import com.example.adopet.Adapters.FavoritesAdapter;
import com.example.adopet.Adapters.PetAdapter;
import com.example.adopet.Interfaces.MeetCallBack;
import com.example.adopet.Interfaces.PetCallBack;
import com.example.adopet.Models.Category;
import com.example.adopet.Models.Pet;
import com.example.adopet.Models.User;
import com.example.adopet.R;
import com.example.adopet.Utilities.DbManager;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private RecyclerView fav_LST_pets;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);


        fav_LST_pets = view.findViewById(R.id.fav_LST_pets);

        user = User.getInstance();

        ArrayList<Pet> favorites = new ArrayList<>();
        for (String petId : user.getFavorites().keySet()) {
                favorites.add(getPetById(petId));
        }



        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fav_LST_pets.setLayoutManager(linearLayoutManager);
        fav_LST_pets.setAdapter(favoritesAdapter);
        favoritesAdapter.setMeetCallBack(pet -> {
            Bundle bundle = new Bundle();
            bundle.putString("ABOUT", pet.getAbout());
            bundle.putString("AGE", String.valueOf(pet.getAge()));
            bundle.putString("BREED", pet.getBreed());
            bundle.putString("IMG", pet.getImg());
            bundle.putString("LOCATION", pet.getLocation());
            bundle.putString("NAME", pet.getName());
            bundle.putString("OWNER", pet.getOwner());
            bundle.putString("PHONE", pet.getPhone());
            bundle.putString("SEX", pet.getSex());

            Intent intent = new Intent(FavoritesFragment.this.getActivity(), PetDetailsActivity.class);
            intent.putExtras(bundle);
            FavoritesFragment.this.startActivity(intent);
        });

        return view;
    }



}