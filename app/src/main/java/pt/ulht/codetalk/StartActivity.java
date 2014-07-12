package pt.ulht.codetalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import pt.ulht.codetalk.R;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        CodeTalk app = (CodeTalk) getApplication();


        if (id == R.id.action_logout) {
            app.getAuthClient().logout();
            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivityForResult(i, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
