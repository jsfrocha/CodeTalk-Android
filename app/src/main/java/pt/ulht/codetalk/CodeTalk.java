package pt.ulht.codetalk;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;
import com.firebase.simplelogin.enums.Error;

import java.util.ArrayList;

import pt.ulht.codetalk.activities.MainActivity;
import pt.ulht.codetalk.activities.StartActivity;

public class CodeTalk extends Application {

    public boolean isLoggedIn;
    private User currentUser;
    private String currentUserUid;
    private String currentUserEmail;
    private Firebase ref = new Firebase("https://codetalking.firebaseio.com");
    private SimpleLogin authClient = new SimpleLogin(ref, this);
    private ArrayList<String> followingGroups;

    private static String INIT_TAG = "INIT APP";

    public ArrayList<String> getFollowingGroups() {
        return followingGroups;
    }

    public void addFollowingGroup(String group) {
        if (followingGroups == null) {
            followingGroups = new ArrayList<String>();
            followingGroups.add(group);
        } else {
            followingGroups.add(group);
        }
    }

    public void removeFollowingGroup(String group) {
        followingGroups.remove(group);
    }

    public Firebase getRef() {
        return this.ref;
    }

    public SimpleLogin getAuthClient() {
        return this.authClient;
    }

    public void setUserLoggedIn(boolean state) {
        this.isLoggedIn = state;
    }

    public boolean getUserLoggedIn() {
        return this.isLoggedIn;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUserUid(String uid) {
        this.currentUserUid = "simplelogin:"+uid;
    }

    public String getCurrentUserUid() {
        return this.currentUserUid;
    }

    public void setCurrentUserEmail(String email) {
        Log.d("APP", "Setting User Email: "+email);
        this.currentUserEmail = email;
    }

    public String getCurrentUserEmail() {
        Log.d("APP", "Getting User Email: "+this.currentUserEmail);
        return this.currentUserEmail;
    }


    public void checkUserAuth(final CodeTalk app) {
        app.authClient.checkAuthStatus(new SimpleLoginAuthenticatedHandler() {
            @Override
            public void authenticated(Error error, User user) {
                if (error != null) {
                    Log.d(INIT_TAG, "Error: "+error);
                }
                else if (user == null) {
                    Log.d(INIT_TAG, "No user logged in");
                    Intent i = new Intent(app, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else {
                    Log.d(INIT_TAG, "User logged in");
                    app.setCurrentUser(user);
                    app.setCurrentUserUid(user.getUserId());
                    app.setCurrentUserEmail(user.getEmail());
                    Intent i = new Intent(app, StartActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            }
        });
    }


}
