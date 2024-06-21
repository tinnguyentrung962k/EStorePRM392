package com.prm392.estoreprm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prm392.estoreprm392.service.model.User;

import kotlin.text.Regex;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "FacebookSignInActivity";

    private FirebaseAuth mAuth = null;
    private FirebaseFirestore db = null;

    //one tap sign in
    private GoogleSignInClient mGoogleSignInClient;
    private BeginSignInRequest signInRequest;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText etUserName = findViewById(R.id.userNameInput);
        EditText etPass = findViewById(R.id.passWordInput);
        Button btnLogin = findViewById(R.id.loginBtn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneOrEmail = etUserName.getText().toString();
                String password = etPass.getText().toString();
                Regex emailRegex = new Regex("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
                if (phoneOrEmail.isEmpty() || !emailRegex.matches(phoneOrEmail) || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
                    return;
                }
                signUpWithFirebase(phoneOrEmail, password);
            }
        });

        setUpLoginWithGoogle();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            doAfterAuth(currentUser);
        }
    }

    private void signUpWithFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            handleCreateUserWithEmailSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void doAfterAuth(FirebaseUser user) {

        Intent intent = new Intent(LoginActivity.this, NewArrivalsActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleCreateUserWithEmailSuccess(FirebaseUser user) {
        // Get the document reference
        DocumentReference docRef = db.collection("users").document(user.getUid());

        // Try to get the document
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Document exists, no need to create a new one
                        Log.d(TAG, "User already exists in Firestore");
                        doAfterAuth(user);
                    } else {
                        // Document does not exist, create a new one
                        Log.d(TAG, "User does not exist in Firestore");
                        // Create a new user document
                        User newUser = new User();
                        newUser.setUid(user.getUid());
                        newUser.setEmail(user.getEmail());
                        newUser.setName(user.getDisplayName());
                        newUser.setPhoneNumber(user.getPhoneNumber());
                        newUser.setProtoUrl(user.getPhotoUrl() == null ? "https://upload.wikimedia.org/wikipedia/commons/9/99/Sample_User_Icon.png" : user.getPhotoUrl().toString());
                        db.collection("users").document(user.getUid())
                                .set(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        doAfterAuth(user);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                    }
                } else {
                    Log.w(TAG, "Error getting document", task.getException());
                }

            }
        });
    }

    private void setUpLoginWithGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        findViewById(R.id.googleLoginbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                // Signed in successfully, now authenticate with Firebase
                firebaseAuthWithGoogle(account);
            } else {
                Log.w(TAG, "handleSignInResult: Google sign in account is null");
            }
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            handleCreateUserWithEmailSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}