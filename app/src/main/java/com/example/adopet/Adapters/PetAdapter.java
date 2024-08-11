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

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    ArrayList<Pet> pets;
    private PetCallBack petCallBack;
    private MeetCallBack meetCallBack;

    public PetAdapter(ArrayList<Pet> pets) {this.pets = pets;}

    public void setPetCallBack(PetCallBack petCallBack) { this.petCallBack = petCallBack; }

    public void setMeetCallBack(MeetCallBack meetCallBack) { this.meetCallBack = meetCallBack; }

    @NonNull
    @Override
    public PetAdapter.PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetAdapter.PetViewHolder holder, int position) {
        Pet pet = getItem(position);
        ImageLoader.getInstance().loadPet(pet.getImg(),holder.pet_IMG_pet);
        holder.pet_LBL_name.setText(pet.getName());
        holder.pet_LBL_breed.setText("Breed: " + pet.getBreed());
        holder.pet_LBL_age.setText("Age: " + pet.getAge());
        holder.pet_LBL_sex.setText(String.valueOf(pet.getSex()));
        holder.pet_LBL_owner.setText("Owner: " + pet.getOwner());
        holder.pet_LBL_location.setText(pet.getLocation());
        User user = User.getInstance();
        if(user.isFavorite(pet.getId()))
            ImageLoader.getInstance().loadFave(R.drawable.heart,holder.pet_IMG_favorite);
        else
            ImageLoader.getInstance().loadFave(R.drawable.empty_heart,holder.pet_IMG_favorite);

    }

    @Override
    public int getItemCount() {return pets == null ? 0 : pets.size();}

    public Pet getItem(int position){
        return pets.get(position);
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView pet_IMG_pet;
        private MaterialTextView pet_LBL_name;
        private MaterialTextView pet_LBL_breed;
        private MaterialTextView pet_LBL_age;
        private MaterialTextView pet_LBL_sex;
        private MaterialTextView pet_LBL_owner;
        private MaterialTextView pet_LBL_location;
        private Button pet_BTN_meet;
        private ShapeableImageView pet_IMG_favorite;

        public PetViewHolder(@NonNull View view) {
            super(view);
            pet_IMG_pet = view.findViewById(R.id.pet_IMG_pet);
            pet_LBL_name = view.findViewById(R.id.pet_LBL_name);
            pet_LBL_breed = view.findViewById(R.id.pet_LBL_breed);
            pet_LBL_age = view.findViewById(R.id.pet_LBL_age);
            pet_LBL_sex = view.findViewById(R.id.pet_LBL_sex);
            pet_LBL_owner = view.findViewById(R.id.pet_LBL_owner);
            pet_LBL_location = view.findViewById(R.id.pet_LBL_location);
            pet_BTN_meet = view.findViewById(R.id.pet_BTN_meet);
            pet_IMG_favorite = view.findViewById(R.id.pet_IMG_favorite);
            pet_IMG_favorite.setOnClickListener(v -> {
                if (petCallBack != null)
                    petCallBack.favoriteButtonClicked(getItem(getAdapterPosition()), getAdapterPosition());
            });
            pet_BTN_meet.setOnClickListener(v -> {
                if (meetCallBack != null)
                    meetCallBack.meetButtonClicked(getItem(getAdapterPosition()));
            });

        }
    }
}

