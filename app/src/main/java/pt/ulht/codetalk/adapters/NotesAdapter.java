package pt.ulht.codetalk.adapters;

import android.app.Activity;
import android.view.View;

import com.firebase.client.Query;

import pt.ulht.codetalk.model.Note;

public class NotesAdapter extends FirebaseListAdapter<Note> {
    public NotesAdapter(Query ref, Activity activity, int layout) {
        super(ref, Note.class, layout, activity);
    }

    @Override
    protected void populateView(View view, Note note) {

    }
}
