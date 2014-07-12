package pt.ulht.codetalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;
import com.firebase.simplelogin.enums.*;
import com.firebase.simplelogin.enums.Error;


public class MainActivity extends Activity {

    private String LOGIN_TAG = "LOGIN USER";
    private String REGISTER_TAG = "REGISTER USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CodeTalk app = (CodeTalk) getApplication();



        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeTalk codeTalkApp = (CodeTalk) getApplication();
                loginUser(codeTalkApp, etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    public void loginUser (final CodeTalk application, final String email, final String password) {
        final SimpleLogin authClient = application.getAuthClient();

        authClient.checkAuthStatus(new SimpleLoginAuthenticatedHandler() {
            @Override
            public void authenticated(com.firebase.simplelogin.enums.Error error, User user) {
                if (error != null) {
                    Log.d(LOGIN_TAG, "Error: " + error);
                } else if (user == null) {
                    Log.d(LOGIN_TAG, "No logged in user, logging-in.");
                    authClient.loginWithEmail(email, password, new SimpleLoginAuthenticatedHandler() {
                        @Override
                        public void authenticated(Error error, User user) {
                            if (error != null) {
                                if (Error.UserDoesNotExist == error) {
                                    Log.d(LOGIN_TAG, "User does not exist");
                                } else {
                                    Log.d(LOGIN_TAG, "Error: " + error);
                                }
                            }
                            else {
                                Log.d(LOGIN_TAG, "User logged into firebase!");
                                Intent i = new Intent(MainActivity.this, StartActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                } else {
                    Log.d(LOGIN_TAG, "User is already logged-in");
                    Intent i = new Intent(MainActivity.this, StartActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    //TODO: Unimplemented & Untested
    public void registerUser (final CodeTalk application, String email, String password) {
        Firebase ref = new Firebase("https://codetalking.firebaseio.com");
        SimpleLogin authClient = new SimpleLogin(ref, getApplicationContext());
        authClient.createUser(email, password, new SimpleLoginAuthenticatedHandler() {
            @Override
            public void authenticated(Error error, User user) {
                if (error != null) {
                    Log.d(REGISTER_TAG, "Error creating new user: "+error);
                }
                else {
                    Log.d(REGISTER_TAG, "User successfully registered");
                    application.setUserLoggedIn(true);
                    application.setCurrentUser(user);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
