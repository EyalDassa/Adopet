package com.example.adopet.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopet.Activities.AddPetActivity;
import com.example.adopet.Interfaces.DeletePetCallBack;
import com.example.adopet.Interfaces.MeetCallBack;
import com.example.adopet.Models.Pet;
import com.example.adopet.R;
import com.example.adopet.Utilities.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ManagePetsAdapter extends RecyclerView.Adapter<ManagePetsAdapter.PetViewHolder> {

    private ArrayList<Pet> pets;
    private DeletePetCallBack deletePetCallBack;

    public ManagePetsAdapter(ArrayList<Pet> pets) {this.pets = pets;}

    public void seDeletePetCallBack(DeletePetCallBack deletePetCallBack) { this.deletePetCallBack = deletePetCallBack; }

    @NonNull
    @Override
    public ManagePetsAdapter.PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_pets_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePetsAdapter.PetViewHolder holder, int position) {
        Pet pet = getItem(position);
        holder.man_LBL_name.setText(pet.getName());
        ImageLoader.getInstance().loadPet(pet.getImg(),holder.man_IMG_pet);
        holder.man_BTN_edit.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddPetActivity.class);
            intent.putExtra("ID", pet.getId());
            intent.putExtra("NAME", pet.getName());
            intent.putExtra("IMG", pet.getImg());
            intent.putExtra("ABOUT", pet.getAbout());
            intent.putExtra("AGE", Integer.toString(pet.getAge()));
            intent.putExtra("LOCATION", pet.getLocation());
            intent.putExtra("isEdit", true);
            holder.itemView.getContext().startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {return pets == null ? 0 : pets.size();}

    public Pet getItem(int position){
        return pets.get(position);
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {



        private ShapeableImageView man_IMG_pet;
        private MaterialTextView man_LBL_name;
        private Button man_BTN_delete;
        private Button man_BTN_edit;

        public PetViewHolder(@NonNull View view) {
            super(view);
            man_IMG_pet = view.findViewById(R.id.man_IMG_pet);
            man_LBL_name = view.findViewById(R.id.man_LBL_name);
            man_BTN_delete = view.findViewById(R.id.man_BTN_delete);
            man_BTN_edit = view.findViewById(R.id.man_BTN_edit);
            man_BTN_delete.setOnClickListener(v -> {

                if (deletePetCallBack != null)
                    deletePetCallBack.deletePetCallBack(getItem(getAdapterPosition()), getAdapterPosition());
            });


        }
    }
}

