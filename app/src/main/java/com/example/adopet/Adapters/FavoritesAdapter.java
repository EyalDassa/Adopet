package com.example.adopet.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopet.Interfaces.MeetCallBack;
import com.example.adopet.Interfaces.PetCallBack;
import com.example.adopet.Models.Pet;
import com.example.adopet.Models.User;
import com.example.adopet.R;
import com.example.adopet.Utilities.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.PetViewHolder> {

    private ArrayList<Pet> pets;
    private MeetCallBack meetCallBack;

    public FavoritesAdapter(ArrayList<Pet> pets) {this.pets = pets;}

    public void setMeetCallBack(MeetCallBack meetCallBack) { this.meetCallBack = meetCallBack; }

    @NonNull
    @Override
    public FavoritesAdapter.PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.PetViewHolder holder, int position) {
        Pet pet = getItem(position);
        ImageLoader.getInstance().loadPet(pet.getImg(),holder.fav_IMG_pet);
        holder.fav_LBL_name.setText(pet.getName());

    }

    @Override
    public int getItemCount() {return pets == null ? 0 : pets.size();}

    public Pet getItem(int position){
        return pets.get(position);
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView fav_IMG_pet;
        private MaterialTextView fav_LBL_name;
        private Button fav_BTN_meet;

        public PetViewHolder(@NonNull View view) {
            super(view);
            fav_IMG_pet = view.findViewById(R.id.fav_IMG_pet);
            fav_LBL_name = view.findViewById(R.id.fav_LBL_name);
            fav_BTN_meet = view.findViewById(R.id.fav_BTN_meet);
            fav_BTN_meet.setOnClickListener(v -> {
                if (meetCallBack != null)
                    meetCallBack.meetButtonClicked(getItem(getAdapterPosition()));
            });

        }
    }
}

