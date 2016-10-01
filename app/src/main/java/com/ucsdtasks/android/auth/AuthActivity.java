package com.ucsdtasks.android.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.ucsdtasks.android.ListActivity;
import com.ucsdtasks.android.MapsActivity;
import com.ucsdtasks.android.R;

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this, ListActivity.class));

        /*
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // User is already signed in. Show task map!
            startActivity(new Intent(this, MapsActivity.class));
            finish();
        }
        else {

            setContentView(R.layout.activity_login);

            // TODO: Move this to a sign-in method that is invoked
            //       on click of SignIn button.
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(AuthUI.FACEBOOK_PROVIDER)
                            .build(),

                    RC_SIGN_IN);
        }

*/
    }
}
