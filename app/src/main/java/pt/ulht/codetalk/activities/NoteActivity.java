package pt.ulht.codetalk.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import pt.ulht.codetalk.CodeTalk;
import pt.ulht.codetalk.R;
import pt.ulht.codetalk.model.Note;

public class NoteActivity extends Activity {

    private String fullNoteTitle;
    private Firebase ref;

    private Note note;

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvCode;
    private TextView tvCreatedAt;
    private TextView tvCreatedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent i = getIntent();
        fullNoteTitle = i.getStringExtra("fullNoteTitle");
        Log.d("NOTE.ACT", "Arriving FullTitle: "+fullNoteTitle);

        setTitle(fullNoteTitle.split("_")[1]);

        ref = new Firebase("https://codetalking.firebaseio.com/notes/"+fullNoteTitle);

        tvTitle = (TextView) findViewById(R.id.tvNoteTitle);
        tvContent = (TextView) findViewById(R.id.tvNoteContent);
        tvCode = (TextView) findViewById(R.id.tvNoteCode);
        tvCreatedAt = (TextView) findViewById(R.id.tvNoteDate);
        tvCreatedBy = (TextView) findViewById(R.id.tvNoteUser);

    }

    @Override
    public void onStart() {
        super.onStart();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title = dataSnapshot.getName().split("_")[1];
                String content = (String) dataSnapshot.child("content").getValue();
                String code = (String) dataSnapshot.child("code").getValue();
                String createdAt = (String) dataSnapshot.child("createdAt").getValue();
                String createdBy = (String) dataSnapshot.child("createdBy").getValue();

                tvTitle.setText(title);
                tvContent.setText(content);
                tvCode.setText(code);
                tvCreatedAt.setText(createdAt);
                tvCreatedBy.setText(createdBy);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        CodeTalk app = (CodeTalk) getApplication();


        if (id == R.id.action_logout) {
            app.getAuthClient().logout();
            Intent i = new Intent(NoteActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(i, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
