package com.example.adopet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.adopet.R;
import com.example.adopet.Models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DetailsActivity extends AppCompatActivity {

    String email;
    String phone;
    String name;
    TextInputLayout details_TXT_fullName;
    TextInputLayout details_TXT_email;
    TextInputLayout details_TXT_phone ;
    Button details_BTN_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        findViews();
        email = getIntent().getStringExtra("EMAIL");
        phone = getIntent().getStringExtra("PHONE");
        name = getIntent().getStringExtra("NAME");
        details_TXT_email.getEditText().setText(email);
        details_TXT_phone.getEditText().setText(phone);
        details_TXT_fullName.getEditText().setText(name);

        if(name != null)
            details_TXT_fullName.getEditText().setEnabled(false);
        if(email != null)
            details_TXT_email.getEditText().setEnabled(false);
        if(phone != null)
            details_TXT_phone.getEditText().setEnabled(false);

        details_BTN_start.setOnClickListener(v -> validateInput());
    }

    private void validateInput() {
        String name = details_TXT_fullName.getEditText().getText().toString();
        String email = details_TXT_email.getEditText().getText().toString();
        String phone = details_TXT_phone.getEditText().getText().toString();
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            details_TXT_fullName.setError("Name is required");
            isValid = false;
        } else {
            details_TXT_fullName.setError(null); // Clear error if valid
        }

        if (TextUtils.isEmpty(email)) {
            details_TXT_email.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            details_TXT_email.setError("Enter a valid email address");
            isValid = false;
        } else {
            details_TXT_email.setError(null); // Clear error if valid
        }

        if (TextUtils.isEmpty(phone)) {
            details_TXT_phone.setError("Phone number is required");
            isValid = false;
        } else if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            details_TXT_phone.setError("Enter a valid phone number");
            isValid = false;
        } else {
            details_TXT_phone.setError(null); // Clear error if valid
        }

        if (isValid) {

            User user = User.getInstance();
            user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usersRef = database.getReference("USERS");

            usersRef.child(user.getId()).setValue(user);

            Bundle bundle = new Bundle();
            bundle.putString("EMAIL",email);
            bundle.putString("PHONE",phone);
            bundle.putString("NAME",name);
            bundle.putString("UID",user.getId());
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }


    private void findViews() {
        details_TXT_fullName = findViewById(R.id.details_TXT_fullName);
        details_TXT_email = findViewById(R.id.details_TXT_email);
        details_TXT_phone = findViewById(R.id.details_TXT_phone);
        details_BTN_start = findViewById(R.id.details_BTN_start);

    }
}