package pt.ulht.codetalk.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;

import pt.ulht.codetalk.CodeTalk;
import pt.ulht.codetalk.R;
import pt.ulht.codetalk.adapters.NotesAdapter;

public class GroupActivity extends ListActivity {

    private String groupName;
    private String currentUserUid;

    private Button btnFollow;
    private Button btnUnfollow;
    private Button btnAddNote;

    private NotesAdapter notesAdapter;

    private Firebase notesRef;
    private Firebase followingRef;
    private Firebase followRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        CodeTalk app = (CodeTalk) getApplication();

        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        setTitle(groupName.split("_")[0]);

        currentUserUid = app.getCurrentUserUid();

        notesRef = new Firebase("https://codetalking.firebaseio.com/groups/"+groupName+"/notes");
        followingRef = new Firebase("https://codetalking.firebaseio.com/users/"+currentUserUid+"/following");
        followRef = new Firebase("https://codetalking.firebaseio.com/users/"+currentUserUid+"/following/"+groupName);

        btnFollow = (Button) findViewById(R.id.btnFollow);
        btnUnfollow = (Button) findViewById(R.id.btnUnfollow);
        btnAddNote = (Button) findViewById(R.id.btnAddNote);

        btnFollow.setVisibility(View.GONE);
        btnUnfollow.setVisibility(View.GONE);

        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(groupName).hasChildren()) {
                    //Is following this group already
                    btnUnfollow.setVisibility(View.VISIBLE);
                }
                else {
                    //Is not following this group yet
                    btnFollow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFollow.setVisibility(View.GONE);
                btnUnfollow.setVisibility(View.VISIBLE);

                Map<String, Boolean> toSet = new HashMap<String, Boolean>();

                toSet.put("isFollowing", true);

                followRef.setValue(toSet);

            }
        });

        btnUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUnfollow.setVisibility(View.GONE);
                btnFollow.setVisibility(View.VISIBLE);

                Firebase ref = new Firebase("http://codetalking.firebaseio.com/users/"+currentUserUid+"/following/"+groupName);
                ref.removeValue(new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.d("Removing", "Removal Complete");
                        } else {
                            Log.d("Removing", "Removal Error: "+firebaseError);
                        }
                    }
                });
            }
        });

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupActivity.this, NewNoteActivity.class);
                i.putExtra("groupName", groupName);
                startActivity(i);
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();

        final ListView listView = getListView();
        notesAdapter = new NotesAdapter(notesRef, this, R.layout.note_item);
        listView.setEmptyView(findViewById(R.id.emptyNotes));
        listView.setAdapter(notesAdapter);

        notesAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(notesAdapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onListItemClick(ListView lv, View v, int position, long id) {
        super.onListItemClick(lv, v, position, id);

        String noteTitle;
        String fullNoteTitle;

        TextView tvTitle = (TextView) v.findViewById(R.id.lvItemNoteTitle);

        noteTitle = tvTitle.getText().toString();

        fullNoteTitle = groupName.split("_")[0] + "_" + noteTitle;
        Log.d("GROUP.ACT", "FullNoteTitle: "+fullNoteTitle);
        Intent i = new Intent(GroupActivity.this, NoteActivity.class);
        i.putExtra("fullNoteTitle", fullNoteTitle);
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        CodeTalk app = (CodeTalk) getApplication();


        if (id == R.id.action_logout) {
            app.getAuthClient().logout();
            Intent i = new Intent(GroupActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(i, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
