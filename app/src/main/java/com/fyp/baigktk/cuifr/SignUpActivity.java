package com.fyp.baigktk.cuifr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.fyp.baigktk.cuifr.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;




public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText FirstEditText, LastEditText;
    private EditText EmailEditText, PasswordEditText;
    private EditText phoneEditText;
    private EditText carMilage;
    private Button mSignUpButton;
    private RadioGroup signUp_gender_radiogrp;
    private RadioButton signUp_gender_male,signUp_gender_female;

    private ImageView profileImage;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 1;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private String gender;
    private int car_milage;
    Spinner dropdown;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //create a list of items for the spinner.
        String[] items = new String[]{"I m Passenger","1300", "1000", "600"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        dropdown=findViewById(R.id.create_Car_milage);

         adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profileImage = (ImageView) findViewById(R.id.profile_image);
        FirstEditText = (EditText) findViewById(R.id.first_name);
        LastEditText = (EditText) findViewById(R.id.last_name);
        EmailEditText = (EditText) findViewById(R.id.create_email);
        PasswordEditText = (EditText) findViewById(R.id.create_password);
        phoneEditText = (EditText) findViewById(R.id.phone_number);
        signUp_gender_female=findViewById(R.id.create_gender_radiobtn_Female);
        signUp_gender_male=findViewById(R.id.create_gender_radiobtn_Male);
        signUp_gender_radiogrp=findViewById(R.id.create_gender_radiogrp);


        mSignUpButton = findViewById(R.id.save_information);

        mSignUpButton.setOnClickListener(this);

        uploadProfileImage();
    }

    public void onStart() {
        super.onStart();

        //Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void SignUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = EmailEditText.getText().toString();
        String password = PasswordEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
        sendVerificationEmail();
                    onAuthSuccess(task.getResult().getUser());

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });


    }








    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
     //                       startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }












    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(EmailEditText.getText().toString())) {
            EmailEditText.setError("Required");
            result = false;
        } else {
            EmailEditText.setError(null);
        }

        if (TextUtils.isEmpty(PasswordEditText.getText().toString())) {
            PasswordEditText.setError("Required");
            result = false;
        } else {
            PasswordEditText.setError(null);
        }

        if (signUp_gender_radiogrp.getCheckedRadioButtonId()==-1)
        {
            Toast.makeText(this, "Please specify your gender", Toast.LENGTH_SHORT).show();
            result=false;
        }
        return result;
    }

    private void onAuthSuccess(FirebaseUser user) {
        String first = FirstEditText.getText().toString();
        String last = LastEditText.getText().toString();
//        car_milage=Integer.parseInt(carMilage.getText().toString());
        int id=signUp_gender_radiogrp.getCheckedRadioButtonId();

        switch(id) {
            case R.id.create_gender_radiobtn_Male:
                gender="Male";
                break;
            case R.id.create_gender_radiobtn_Female:
                gender="Female";
                break;
            default:
                gender="Male";
        }


        String phone = "";
        if (phoneEditText != null) {
            phone = phoneEditText.getText().toString();
        }


        String imageURL = "";

        if (filePath != null) {
            StorageReference ref = storageReference.child("profileImages/" + UUID.randomUUID().toString());
            ref.putFile(filePath);
            imageURL = UUID.randomUUID().toString();
//            writeNewUser(user.getUid(),user.getEmail(), first, last, phone, imageURL);
        }
        String text = dropdown.getSelectedItem().toString();
        int milage;
        if(text=="600")
        {
            milage=15;
        }
        else if(text=="1000")
            {
                milage=12;

            }
            else if(text=="1300")
                {
                    milage=10;
                }
            else {
                    milage = 0;
                }

        writeNewUser(user.getUid(),user.getEmail(), first, last, phone, imageURL,gender,milage);
//
//        //Go to MainActivity
//        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
//        intent.putExtra("callingActivity", "SignUpActivity");
//        startActivity(intent);
//        finish();
    }

    //Writes user's email in users table
    private void writeNewUser(String userId, String email, String first, String last, String phone, String filepath,String gender,int milage) {
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setFirstName(first);
        user.setLastName(last);
        user.setPhoneNumber(phone);
        user.setImageURL(filepath);
        user.setGender(gender);
        user.setCarMilage(milage);

        mDatabase.child("users").child(userId).setValue(user);
    }

//    private void getUsername(String email){
//
//    }

    private void uploadProfileImage(){

        //When ImageView profileImage is pressed, user can choose image from their device gallery
        profileImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*"); //Intent type is set to image
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_information:
                SignUp();
                break;

        }
    }
}

