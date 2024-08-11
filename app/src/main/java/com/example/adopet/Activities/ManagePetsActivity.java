package com.example.adopet.Activities;

import static com.example.adopet.Utilities.DbManager.deletePet;
import static com.example.adopet.Utilities.DbManager.getPetById;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adopet.Adapters.ManagePetsAdapter;
import com.example.adopet.Interfaces.DeletePetCallBack;
import com.example.adopet.Models.Pet;
import com.example.adopet.Models.User;
import com.example.adopet.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ManagePetsActivity extends AppCompatActivity {

    private Button man_BTN_back;
    private RecyclerView man_LST_pets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pets);

        findViews();

        man_BTN_back.setOnClickListener(v -> {
            finish();
        });

        ArrayList<Pet> pets = new ArrayList<>();
        for (String petId : User.getInstance().getPets().keySet()) {
            pets.add(getPetById(petId));
        }
        Log.d("ManagePets", "pets: " + pets);
        ManagePetsAdapter adapter = new ManagePetsAdapter(pets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        man_LST_pets.setLayoutManager(linearLayoutManager);
        man_LST_pets.setAdapter(adapter);
        adapter.seDeletePetCallBack((pet, position) ->
                new MaterialAlertDialogBuilder(ManagePetsActivity.this)
                .setTitle("Delete Pet")
                .setMessage("Are you sure you want to delete " + pet.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deletePet(pet.getId(), User.getInstance().getId());
                    User.getInstance().getPets().remove(pet.getId());
                    pets.remove(position);
                    adapter.notifyItemRemoved(position);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show());

    }

    private void findViews() {
        man_BTN_back = findViewById(R.id.man_BTN_back);
        man_LST_pets = findViewById(R.id.man_LST_pets);
    }
}