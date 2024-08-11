package com.example.adopet.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopet.Activities.MainActivity;
import com.example.adopet.Activities.PetDetailsActivity;
import com.example.adopet.Activities.SignInActivity;
import com.example.adopet.Adapters.CategoryAdapter;
import com.example.adopet.Adapters.PetAdapter;
import com.example.adopet.Interfaces.CategoryCallBack;
import com.example.adopet.Interfaces.MeetCallBack;
import com.example.adopet.Interfaces.PetCallBack;
import com.example.adopet.Models.Category;
import com.example.adopet.Models.Pet;
import com.example.adopet.Models.User;
import com.example.adopet.R;
import com.example.adopet.Utilities.DbManager;

import java.util.ArrayList;

public class PetsFragment extends Fragment {

    private RecyclerView pets_LST_pets;
    private User user;
    private static PetAdapter petAdapter;
    private DbManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pets, container, false);


        pets_LST_pets = view.findViewById(R.id.pets_LST_pets);

        dbManager = DbManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        pets_LST_pets.setLayoutManager(linearLayoutManager);
        setCategory(dbManager.getCategories().get(0));
        return view;
    }

    public static PetAdapter getPetAdapter(){
        return petAdapter;
    }

    public void setCategory(Category category) {
        switch (category.getName()) {
            case "Turtles":
                ArrayList<Pet> turtles = new ArrayList<>();
                for(Pet pet : DbManager.getPopular()){
                    if(pet.getType().equalsIgnoreCase("turtle"))
                        turtles.add(pet);
                }
                petAdapter = new PetAdapter(turtles);
                break;
            case "Rabbits":
                ArrayList<Pet> rabbits = new ArrayList<>();
                for(Pet pet : DbManager.getPopular()){
                    if(pet.getType().equalsIgnoreCase("rabbit"))
                        rabbits.add(pet);
                }
                petAdapter = new PetAdapter(rabbits);
                break;
            case "Parrots":
                ArrayList<Pet> parrots = new ArrayList<>();
                for(Pet pet : DbManager.getPopular()){
                    if(pet.getType().equalsIgnoreCase("parrot"))
                        parrots.add(pet);
                }
                petAdapter = new PetAdapter(parrots);
                break;
            case "Fish":
                ArrayList<Pet> fish = new ArrayList<>();
                for(Pet pet : DbManager.getPopular()){
                    if(pet.getType().equalsIgnoreCase("fish"))
                        fish.add(pet);
                }
                petAdapter = new PetAdapter(fish);
                break;
            case "Cats":
                ArrayList<Pet> cats = new ArrayList<>();
                for(Pet pet : DbManager.getPopular()){
                    if(pet.getType().equalsIgnoreCase("cat"))
                        cats.add(pet);
                }
                petAdapter = new PetAdapter(cats);
                break;
            case "Dogs":
                ArrayList<Pet> dogs = new ArrayList<>();
                for(Pet pet : DbManager.getPopular()){
                    if(pet.getType().equalsIgnoreCase("dog"))
                        dogs.add(pet);
                }
                petAdapter = new PetAdapter(dogs);
                break;
            case "Popular":
                petAdapter = new PetAdapter(DbManager.getPopular());
                break;
            default:
                petAdapter = new PetAdapter(DbManager.getPopular());
                break;
        }
        pets_LST_pets.setAdapter(petAdapter);
        petAdapter.setPetCallBack((pet, position) -> {
            user = User.getInstance();
            DbManager.toggleFavoritePet(user.getId(), pet.getId());
            user.toggleFavorite(pet.getId());
            petAdapter.notifyItemChanged(position);
        });
        petAdapter.setMeetCallBack(pet -> {
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

            Intent intent = new Intent(PetsFragment.this.getActivity(), PetDetailsActivity.class);
            intent.putExtras(bundle);
            PetsFragment.this.startActivity(intent);


        });

    }

}