package pt.ulht.codetalk.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.simplelogin.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.ulht.codetalk.CodeTalk;
import pt.ulht.codetalk.R;
import pt.ulht.codetalk.adapters.GroupsAdapter;
import pt.ulht.codetalk.services.FirebaseBackgroundService;

public class StartActivity extends ListActivity {

    private String START_TAG = "START.ACT";
    private CodeTalk app;
    private GroupsAdapter groupsAdapter;
    private Firebase ref;
    private Firebase followRef;
    private SharedPreferences mShared;
    private Set<String> followedGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        app = (CodeTalk) getApplication();
        User user = app.getCurrentUser();
        mShared = getSharedPreferences("pt.ulht.codetalk", Context.MODE_PRIVATE);
        ref = new Firebase("https://codetalking.firebaseio.com/users/"+app.getCurrentUserUid()+"/allowedGroups");

        followRef = new Firebase("https://codetalking.firebaseio.com/users/"+app.getCurrentUserUid()+"/following");
        followRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences.Editor editor = mShared.edit();
                followedGroups = new HashSet<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    followedGroups.add(child.getName());
                    Log.d(START_TAG, "Following Group: "+child.getName());
                }
                editor.putStringSet("followedGroups", followedGroups);
                editor.commit();

                Log.d(START_TAG, "Starting Service");
                //Start notifications service
                Intent i = new Intent(FirebaseBackgroundService.class.getName());
                startService(i);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        //Start notifications service
//        Intent i = new Intent(FirebaseBackgroundService.class.getName());
//        i.putExtra("currentUserUid", app.getCurrentUserUid());
//        startService(i);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Setup list adapter
        final ListView listView = getListView();
        groupsAdapter = new GroupsAdapter(ref, this, R.layout.group_item);
        listView.setAdapter(groupsAdapter);

        groupsAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(groupsAdapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView lv, View v, int position, long id) {
        super.onListItemClick(lv, v, position, id);

        String fullName;
        TextView tvFull = (TextView) v.findViewById(R.id.lvItemGroupFullName);

        fullName = tvFull.getText().toString();

        Intent i = new Intent(StartActivity.this, GroupActivity.class);
        i.putExtra("groupName", fullName);
        startActivity(i);

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
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(i, 0);
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent i = new Intent(StartActivity.this, FollowingActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
