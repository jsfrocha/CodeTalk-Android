package pt.ulht.codetalk.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;

import pt.ulht.codetalk.CodeTalk;
import pt.ulht.codetalk.R;

public class FollowingActivity extends ListActivity {

    private String currentUserUid;

    private Firebase ref;

    private ArrayList<String> followingGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        CodeTalk app = (CodeTalk) getApplication();
        currentUserUid = app.getCurrentUserUid();

        setTitle("Groups Followed");

        followingGroups = new ArrayList<String>();

        ref = new Firebase("https://codetalking.firebaseio.com/users/"+currentUserUid+"/following");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("Follow", "Group: "+child.getName());
                    followingGroups.add(child.getName().split("_")[0]);
                }

                final ListView listView = getListView();
                ArrayAdapter<String> simpleAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.following_item, R.id.lvItemFollowedGroup, followingGroups);
                listView.setAdapter(simpleAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String title;
                        String currentUserUid;

                        TextView tvTitle = (TextView) view.findViewById(R.id.lvItemFollowedGroup);
                        CodeTalk app = (CodeTalk) getApplication();

                        title = tvTitle.getText().toString();
                        currentUserUid = app.getCurrentUserUid();

                        Intent i = new Intent(FollowingActivity.this, GroupActivity.class);
                        i.putExtra("groupName", title + "_" + currentUserUid);
                        startActivity(i);
                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();



    }

}
