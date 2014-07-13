package pt.ulht.codetalk.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pt.ulht.codetalk.CodeTalk;
import pt.ulht.codetalk.R;
import pt.ulht.codetalk.model.Note;

public class NewNoteActivity extends Activity {

    private String groupName;
    private String currentUserEmail;

    private EditText etNoteTitle;
    private EditText etNoteContent;

    private Button btnNewNote;

    private Firebase groupNotesListRef;
    private Firebase notesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        CodeTalk app = (CodeTalk) getApplication();

        Intent i = getIntent();
        groupName = i.getStringExtra("groupName");
        setTitle("Add New Note on "+groupName.split("_")[0]);

        currentUserEmail = app.getCurrentUserEmail();

        etNoteTitle = (EditText) findViewById(R.id.etNoteTitle);
        etNoteContent = (EditText) findViewById(R.id.etNoteContent);
        btnNewNote = (Button) findViewById(R.id.btnNewNote);

        groupNotesListRef = new Firebase("https://codetalking.firebaseio.com/groups/"+groupName+"/notes");
        notesRef = new Firebase("https://codetalking.firebaseio.com/notes");
    }

    @Override
    public void onStart() {
        super.onStart();

        btnNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title;
                String content;

                title = etNoteTitle.getText().toString();
                content = etNoteContent.getText().toString();

                if (!title.equalsIgnoreCase("") && !content.equalsIgnoreCase("")) {
                    //Add to Groups/Notes
                    Firebase listRef = groupNotesListRef.push();

                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM LLL yyyy kk:mm:ss zzz"); /*Sun, 13 Jul 2014 02:13:05 GMT*/
                    String currentDate = sdf.format(Calendar.getInstance().getTime());

                    Map<String, String> newNote = new HashMap<String, String>();

                    newNote.put("code", "");
                    newNote.put("content", content);
                    newNote.put("createdAt", currentDate);
                    newNote.put("createdBy", currentUserEmail);
                    newNote.put("title", title);

                    listRef.setValue(newNote);

                    //Add to /Notes
                    Firebase newNoteRef = notesRef.child(groupName.split("_")[0] + "_" + title);

                    Map<String, String> newNote2 = new HashMap<String, String>();

                    newNote2.put("code", "");
                    newNote2.put("content", content);
                    newNote2.put("createdAt", currentDate);
                    newNote2.put("createdBy", currentUserEmail);

                    newNoteRef.setValue(newNote2);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_note, menu);
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
