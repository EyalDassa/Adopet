package com.example.adopet.Activities;

import static com.example.adopet.Utilities.DbManager.getPetById;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adopet.Models.Pet;
import com.example.adopet.Models.User;
import com.example.adopet.R;
import com.example.adopet.Utilities.DbManager;
import com.example.adopet.Utilities.ImageLoader;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPetActivity extends AppCompatActivity {

    private Uri imageUri;

    private TextInputEditText addPet_TXT_name, addPet_TXT_breed, addPet_TXT_about, addPet_TXT_age, addPet_TXT_city;
    private RadioGroup addPet_RADIOGROUP_type, addPet_RADIOGROUP_sex;
    private ImageView addPet_IMG_pet;
    private Button addPet_BTN_img, petAdd_BTN_back, addPet_BTN_submit;
    private CircularProgressIndicator petAdd_PRG_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        findViews();

        addPet_BTN_img.setOnClickListener(v -> editImage());

        petAdd_BTN_back.setOnClickListener(v -> {
            finish();
        });
        if (getIntent().getExtras().getBoolean("isEdit")) {
            addPet_BTN_submit.setText("Update");
            setUpdateForm();
            addPet_BTN_submit.setOnClickListener(v -> {
                if (validateUpdateForm()) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    petAdd_PRG_loading.setVisibility(View.VISIBLE);
                    submitUpdateForm();
                }
            });
        } else {
            addPet_BTN_submit.setOnClickListener(v -> {
                if (validateForm()) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    petAdd_PRG_loading.setVisibility(View.VISIBLE);
                    submitForm();
                }
            });
        }

    }

    private void submitUpdateForm() {
        String petId = getIntent().getStringExtra("ID");
        String name = addPet_TXT_name.getText().toString().trim();
        String about = addPet_TXT_about.getText().toString().trim();
        int age = Integer.parseInt(addPet_TXT_age.getText().toString().trim());
        String location = addPet_TXT_city.getText().toString().trim();

        Pet pet = getPetById(petId);
        pet.setName(name).setAbout(about).setAge(age).setLocation(location);
        DbManager.updatePet(pet);
        if (imageUri != null){
            uploadPetImage(pet, imageUri);
        }
        else {
            endActivityReturnToManagePets();
        }
    }

    private boolean validateUpdateForm() {
        String name = addPet_TXT_name.getText().toString().trim();
        String about = addPet_TXT_about.getText().toString().trim();
        String ageStr = addPet_TXT_age.getText().toString().trim();
        String city = addPet_TXT_city.getText().toString().trim();
        // Validate Name
        if (TextUtils.isEmpty(name)) {
            addPet_TXT_name.setError("Please enter the pet's name");
            return false;
        }
        // Validate About
        if (TextUtils.isEmpty(about)) {
            addPet_TXT_about.setError("Please provide some details about the pet");
            return false;
        }

        if (about.length() > 235) {
            addPet_TXT_about.setError("About should be less than 235 characters long");
            return false;
        }

        // Validate Age
        if (TextUtils.isEmpty(ageStr)) {
            addPet_TXT_age.setError("Please enter the pet's age");
            return false;
        }

        // Validate City
        if (TextUtils.isEmpty(city)) {
            addPet_TXT_city.setError("Please select a city");
            return false;
        }

        return true;
    }

    private void setUpdateForm() {
        addPet_RADIOGROUP_type.setVisibility(View.GONE);
        addPet_RADIOGROUP_sex.setVisibility(View.GONE);
        addPet_TXT_breed.setVisibility(View.GONE);
        addPet_TXT_name.setText(getIntent().getStringExtra("NAME"));
        addPet_TXT_about.setText(getIntent().getStringExtra("ABOUT"));
        addPet_TXT_age.setText(getIntent().getStringExtra("AGE"));
        addPet_TXT_city.setText(getIntent().getStringExtra("LOCATION"));
        ImageLoader.getInstance().loadPet(getIntent().getStringExtra("IMG"), addPet_IMG_pet);
    }

    private void findViews() {
        addPet_TXT_name = findViewById(R.id.addPet_TXT_name);
        addPet_TXT_breed = findViewById(R.id.addPet_TXT_breed);
        addPet_TXT_about = findViewById(R.id.addPet_TXT_about);
        addPet_TXT_age = findViewById(R.id.addPet_TXT_age);
        addPet_TXT_city = findViewById(R.id.addPet_TXT_city);
        addPet_RADIOGROUP_type = findViewById(R.id.addPet_RADIOGROUP_type);
        addPet_RADIOGROUP_sex = findViewById(R.id.addPet_RADIOGROUP_sex);
        addPet_IMG_pet = findViewById(R.id.addPet_IMG_pet);
        addPet_BTN_submit = findViewById(R.id.addPet_BTN_submit);
        addPet_BTN_img = findViewById(R.id.addPet_BTN_img);
        petAdd_BTN_back = findViewById(R.id.petAdd_BTN_back);
        petAdd_PRG_loading = findViewById(R.id.petAdd_PRG_loading);
    }


    private boolean validateForm() {
        String name = addPet_TXT_name.getText().toString().trim();
        String breed = addPet_TXT_breed.getText().toString().trim();
        String about = addPet_TXT_about.getText().toString().trim();
        String ageStr = addPet_TXT_age.getText().toString().trim();
        String city = addPet_TXT_city.getText().toString().trim();
        int selectedTypeId = addPet_RADIOGROUP_type.getCheckedRadioButtonId();
        int selectedSexId = addPet_RADIOGROUP_sex.getCheckedRadioButtonId();

        // Validate Name
        if (TextUtils.isEmpty(name)) {
            addPet_TXT_name.setError("Please enter the pet's name");
            return false;
        }

        // Validate Breed
        if (TextUtils.isEmpty(breed)) {
            addPet_TXT_breed.setError("Please enter the pet's breed");
            return false;
        }

        // Validate About
        if (TextUtils.isEmpty(about)) {
            addPet_TXT_about.setError("Please provide some details about the pet");
            return false;
        }

        if (about.length() > 235) {
            addPet_TXT_about.setError("About should be less than 235 characters long");
            return false;
        }

        // Validate Age
        if (TextUtils.isEmpty(ageStr)) {
            addPet_TXT_age.setError("Please enter the pet's age");
            return false;
        }

        // Validate City
        if (TextUtils.isEmpty(city)) {
            addPet_TXT_city.setError("Please select a city");
            return false;
        }

        // Validate Type
        if (selectedTypeId == -1) {
            Toast.makeText(this, "Please select the pet type", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Sex
        if (selectedSexId == -1) {
            Toast.makeText(this, "Please select the pet's sex", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Image
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image of the pet", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void submitForm() {
        String petId = generateUniqueId();
        String name = addPet_TXT_name.getText().toString().trim();
        String breed = addPet_TXT_breed.getText().toString().trim();
        String about = addPet_TXT_about.getText().toString().trim();
        int age = Integer.parseInt(addPet_TXT_age.getText().toString().trim());
        String location = addPet_TXT_city.getText().toString().trim();
        int selectedTypeId = addPet_RADIOGROUP_type.getCheckedRadioButtonId();
        int selectedSexId = addPet_RADIOGROUP_sex.getCheckedRadioButtonId();
        RadioButton selectedType = findViewById(selectedTypeId);
        RadioButton selectedSex = findViewById(selectedSexId);
        String type = selectedType.getText().toString();
        String sex = selectedSex.getText().toString();
        String owner = User.getInstance().getName();
        String ownerId = User.getInstance().getId();
        String phone = User.getInstance().getPhone();
        // Create a new Pet object with the submitted
        Pet pet = new Pet().setId(petId).setName(name).setBreed(breed).setAbout(about)
                .setImg(imageUri.toString()).setAge(age).setLocation(location).setPhone(phone)
                .setOwner(owner).setType(type).setSex(sex).setOwnerId(ownerId);

        DbManager.newPet(pet);
        User.getInstance().getPets().put(petId, 1);
        uploadPetImage(pet, imageUri);
    }

    private String generateUniqueId() {
        return String.valueOf(System.currentTimeMillis());
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
                    imageUri = uri;
                    showImage(uri);
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    private void showImage(Uri uri) {
        addPet_IMG_pet.setImageURI(uri);
    }

    private void uploadPetImage(Pet pet, Uri uri) {
        String petId = pet.getId();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("Pets_img").child(petId + ".jpg");

        UploadTask uploadTask = imageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(exception ->
                Toast.makeText(AddPetActivity.this, "Failed: " + exception.getMessage(),
                        Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(taskSnapshot ->
                imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                    String imageUrl = task.getResult().toString();
                    pet.setImg(imageUrl);
                    Log.d("AddPetActivity", "Image URL: " + imageUrl);
                    DbManager.updatePetImage(imageUrl, petId, res -> {
                        if(getIntent().getExtras().getBoolean("isEdit"))
                            endActivityReturnToManagePets();
                        else
                            endActivityReturnToMain();
                    });
                }));
    }

    private void endActivityReturnToMain() {
        petAdd_PRG_loading.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Intent intent = new Intent(AddPetActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void endActivityReturnToManagePets() {
        petAdd_PRG_loading.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Intent intent = new Intent(AddPetActivity.this, ManagePetsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
