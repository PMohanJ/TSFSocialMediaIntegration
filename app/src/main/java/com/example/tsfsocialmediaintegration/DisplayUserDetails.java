package com.example.tsfsocialmediaintegration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class DisplayUserDetails extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    TextView nameView, emailView, profileIdView, phoneNoView;
    ImageView profilePhotoView;
    Button GsignOutView, FsignOutView;
    String name, profilePhoto,email,profileId, phoneNo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        nameView = findViewById(R.id.name);
        emailView = findViewById(R.id.email);
        profileIdView = findViewById(R.id.profileid);
        phoneNoView = findViewById(R.id.phoneNo);
        profilePhotoView = findViewById(R.id.profile_photo);
        GsignOutView = findViewById(R.id.G_sign_out);
        FsignOutView = findViewById(R.id.F_sign_out);

        //Extracting labels from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("userName");
            email = extras.getString("userEmail");
            phoneNo = extras.getString("userPhoneNo");
            profilePhoto = extras.getString("userProfilePhoto");
            profileId = extras.getString("userProfileId");

        }

        //setting up the fields with respective labels
        nameView.setText(name);
        emailView.setText(email);
        if (phoneNo != null)
            phoneNoView.setText(phoneNo);
        else
            phoneNoView.setText(R.string.no_number);
        profileIdView.setText(profileId);
        //method to retrieve profile by the url
        Picasso.get()
                .load(profilePhoto)
                .placeholder(R.drawable.profile_photo_placeholder)
                .resize(100, 100)
                .centerCrop()
                .into(profilePhotoView);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            FsignOutView.setVisibility(View.GONE);
            GsignOutView.setOnClickListener(v -> {
                signOut();
            });
        }
        else{
            GsignOutView.setVisibility(View.GONE);
            FsignOutView.setOnClickListener(v -> {
                signOut();
            });
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(
                task -> {
                    Toast.makeText(DisplayUserDetails.this, "Sign out successful",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DisplayUserDetails.this, MainActivity.class));
                    finish();
                });
    }

}