package pt.ulht.codetalk.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;

import pt.ulht.codetalk.CodeTalk;
import pt.ulht.codetalk.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */

        new PrefetchData().execute();

    }

    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        private String ASYNC_TAG = "ASYNC";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            /*
             * Will make http call here This call will download required data
             * before launching the app
             * example:
             * 1. Downloading and storing in SQLite
             * 2. Downloading images
             * 3. Fetching and parsing the xml / json
             * 4. Sending device information to server
             * 5. etc.,
             */
            final CodeTalk app = (CodeTalk) getApplication();

            app.getAuthClient().checkAuthStatus(new SimpleLoginAuthenticatedHandler() {
                @Override
                public void authenticated(com.firebase.simplelogin.enums.Error error, User user) {
                    if (error != null) {
                        Log.d(ASYNC_TAG, "Error: " + error);
                    }
                    else if (user == null) {
                        Log.d(ASYNC_TAG, "No user logged in");
                        Intent i = new Intent(app, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    else {
                        Log.d(ASYNC_TAG, "User logged in");
                        app.setCurrentUser(user);
                        app.setCurrentUserUid(user.getUserId());
                        app.setCurrentUserEmail(user.getEmail());
                        Intent i = new Intent(app, StartActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



        }

    }

}
