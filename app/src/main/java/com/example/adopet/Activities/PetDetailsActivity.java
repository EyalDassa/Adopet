package com.example.adopet.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.adopet.R;
import com.example.adopet.Utilities.ImageLoader;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;


public class PetDetailsActivity extends AppCompatActivity {

    private Button det_BTN_back;
    private Button det_BTN_max;
    private RelativeLayout det_LAY_imgLarge;
    private Button det_BTN_min;
    private MaterialTextView det_LBL_petName;
    private ShapeableImageView det_IMG_pet;
    private ShapeableImageView det_IMG_petLarge;
    private MaterialTextView det_LBL_breed;
    private MaterialTextView det_LBL_aboutData;
    private MaterialTextView det_LBL_sex;
    private MaterialTextView det_LBL_age;
    private MaterialTextView det_LBL_location;
    private MaterialTextView det_LBL_owner;
    private Button det_BTN_message;
    private Button det_BTN_call;
    private String about;
    private String age;
    private String breed;
    private String img;
    private String location;
    private String name;
    private String owner;
    private String phone;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);
        findViews();
        about = getIntent().getStringExtra("ABOUT");
        age = getIntent().getStringExtra("AGE");
        breed = getIntent().getStringExtra("BREED");
        img = getIntent().getStringExtra("IMG");
        location = getIntent().getStringExtra("LOCATION");
        name = getIntent().getStringExtra("NAME");
        owner = getIntent().getStringExtra("OWNER");
        phone = getIntent().getStringExtra("PHONE");
        sex = getIntent().getStringExtra("SEX");
        setViews();
    }

    private void setViews() {
        det_LBL_petName.setText(name);
        det_LBL_breed.setText(breed);
        det_LBL_aboutData.setText(about);
        det_LBL_sex.setText(sex);
        det_LBL_age.setText(age);
        det_LBL_location.setText(location);
        det_LBL_owner.setText(owner);
        Log.d("Pet Details", "img: " + img);
        ImageLoader.getInstance().loadPet(img,det_IMG_pet);
        ImageLoader.getInstance().loadPet(img,det_IMG_petLarge);
        det_BTN_call.setOnClickListener(v -> {
            callOwner();
        });
        det_BTN_message.setOnClickListener(v -> {
            composeSmsMessage(phone);
        });
        det_BTN_back.setOnClickListener(v -> {
            finish();
        });
        det_BTN_max.setOnClickListener(v -> {
            det_LAY_imgLarge.setVisibility(RelativeLayout.VISIBLE);
            det_BTN_max.setEnabled(false);
            det_BTN_max.setVisibility(Button.INVISIBLE);
            det_BTN_call.setEnabled(false);
            det_BTN_message.setEnabled(false);

        });
        det_BTN_min.setOnClickListener(v -> {
            det_LAY_imgLarge.setVisibility(RelativeLayout.GONE);
            det_BTN_max.setEnabled(true);
            det_BTN_max.setVisibility(Button.VISIBLE);
            det_BTN_call.setEnabled(true);
            det_BTN_message.setEnabled(true);
        });
    }


    private void findViews() {
        det_BTN_back = findViewById(R.id.det_BTN_back);
        det_LBL_petName = findViewById(R.id.det_LBL_petName);
        det_IMG_pet = findViewById(R.id.det_IMG_pet);
        det_LBL_breed = findViewById(R.id.det_LBL_breed);
        det_LBL_aboutData = findViewById(R.id.det_LBL_aboutData);
        det_LBL_sex = findViewById(R.id.det_LBL_sex);
        det_LBL_age = findViewById(R.id.det_LBL_age);
        det_LBL_location = findViewById(R.id.det_LBL_location);
        det_LBL_owner = findViewById(R.id.det_LBL_owner);
        det_BTN_message = findViewById(R.id.det_BTN_message);
        det_BTN_call = findViewById(R.id.det_BTN_call);
        det_BTN_max = findViewById(R.id.det_BTN_max);
        det_LAY_imgLarge = findViewById(R.id.det_LAY_imgLarge);
        det_BTN_min = findViewById(R.id.det_BTN_min);
        det_IMG_petLarge = findViewById(R.id.det_IMG_petLarge);

    }

    public void composeSmsMessage(String phoneNumber) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Send Message to Owners")
                .setMessage("Send message to " +  owner + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("smsto:"+phoneNumber)); // This ensures only SMS apps respond
                    intent.putExtra("sms_body",
                            "Hi " + owner + ", I came across your post about " + name + " " +
                                    "on Adopet! I’m really interested in adopting them. Could we discuss the details? Thanks!");
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    public void callOwner(){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Call Owners")
                .setMessage("Call " +  owner + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}