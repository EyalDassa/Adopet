package com.example.adopet.Fragments;


import static com.example.adopet.Utilities.DbManager.deleteUserImage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.adopet.Activities.MainActivity;
import com.example.adopet.Activities.ManagePetsActivity;
import com.example.adopet.Activities.SignInActivity;
import com.example.adopet.Interfaces.UserDetailsCallBack;
import com.example.adopet.Models.User;
import com.example.adopet.R;
import com.example.adopet.Utilities.DbManager;
import com.example.adopet.Utilities.ImageLoader;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;


public class UserFragment extends Fragment {

    private ShapeableImageView user_IMG_user;
    private Button user_BTN_editImage;
    private MaterialTextView user_LBL_name;
    private TextInputLayout user_TXT_email;
    private Button user_BTN_removeImage;
    private Button user_BTN_dropdown;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        findViews(view);
        user = User.getInstance();
        user_LBL_name.setText(user.getName());
        Log.d("Firebasee", "User image: " + user.getImage());
        ImageLoader.getInstance().loadUser(user.getImage(), user_IMG_user);
        user_TXT_email.getEditText().setText(user.getEmail());
        user_BTN_editImage.setOnClickListener(v -> {
            editImage();
        });
        user_BTN_removeImage.setOnClickListener(v -> {
            deleteUserImage(res -> refreshUserImage());
        });
        FavoritesFragment fragmentFavorites = new FavoritesFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.user_LAY_pets, fragmentFavorites)
                .commit();
        user_BTN_dropdown.setOnClickListener(v -> {
            showDropdownMenu(v);
        });
        return view;
    }

    private void deleteAccount() {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete this account?\n" + "All your data will be permanently deleted.\n" + "This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    HashMap<String, Integer> favorites = new HashMap<>();
                    favorites.putAll(user.getFavorites());
                    Log.d("User Fragment", "Favorites: " + favorites);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = firebaseUser.getUid();
                    firebaseUser.delete()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Log.d("User Fragment", "User account cleaned.");
                                    DbManager.deleteUser(userId,favorites, res -> {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference userRef = database.getReference("USERS").child(userId);
                                        userRef.removeValue();
                                        user.cleanUserData();
                                        startActivity(new Intent(getActivity(), SignInActivity.class));
                                        getActivity().finish();
                                    });
                                }
                            });

                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();

    }

    private void signOut(User user) {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    AuthUI.getInstance()
                            .signOut(getActivity())
                            .addOnCompleteListener(task -> {
                                user.cleanUserData();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                getActivity().finish();
                            });
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void showDropdownMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), anchor);
        popupMenu.getMenuInflater().inflate(R.menu.dropdown_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.dropdown_signOut) {
                signOut(user);
                return true;
            } else if (item.getItemId() == R.id.dropdown_deleteAcc) {
                deleteAccount();
                return true;
            } else if (item.getItemId() == R.id.dropdown_pets) {
                Intent intent = new Intent(getActivity(), ManagePetsActivity.class);
                intent.putExtra("fragment", "pets");
                startActivity(intent);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    private void findViews(View view) {
        user_IMG_user = view.findViewById(R.id.user_IMG_user);
        user_BTN_editImage = view.findViewById(R.id.user_BTN_editImage);
        user_LBL_name = view.findViewById(R.id.user_LBL_name);
        user_TXT_email = view.findViewById(R.id.user_TXT_email);
        user_BTN_removeImage = view.findViewById(R.id.user_BTN_removeImage);
        user_BTN_dropdown = view.findViewById(R.id.user_BTN_dropdown);
    }

    private void editImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    uploadImage(uri);
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    private void uploadImage(Uri uri) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("Users_img").child(userUid + ".jpg");

        UploadTask uploadTask = imageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception ->
                Toast.makeText(getActivity(), "Failed: " +
                        exception.getMessage(), Toast.LENGTH_SHORT).show()).
                addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl().
                                addOnCompleteListener(task -> {
                            String imageUrl = task.getResult().toString();
                            DbManager.updateUserImage(imageUrl, res -> refreshUserImage());
                        }));
    }

    private void refreshUserImage() {
        DbManager.getUserImage(imageUrl ->
                ImageLoader.getInstance().loadUser(imageUrl, user_IMG_user));
    }


}
