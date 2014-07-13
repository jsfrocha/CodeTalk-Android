package pt.ulht.codetalk.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import org.w3c.dom.Text;

import java.util.Date;

import pt.ulht.codetalk.R;
import pt.ulht.codetalk.model.Note;

public class NotesAdapter extends FirebaseListAdapter<Note> {
    public NotesAdapter(Query ref, Activity activity, int layout) {
        super(ref, Note.class, layout, activity);
    }

    @Override
    protected void populateView(View view, Note note) {

        String content = note.getContent();
        String createdAt = note.getCreatedAt();
        String createdBy = note.getCreatedBy();
        String title = note.getTitle();

        TextView tvContent = (TextView) view.findViewById(R.id.lvItemNoteContent);
        TextView tvCreatedAt = (TextView) view.findViewById(R.id.lvItemNoteDate);
        TextView tvCreatedBy = (TextView) view.findViewById(R.id.lvItemNoteUser);
        TextView tvTitle = (TextView) view.findViewById(R.id.lvItemNoteTitle);

        tvContent.setText(content);
        tvCreatedAt.setText(createdAt);
        tvCreatedBy.setText(createdBy);
        tvTitle.setText(title);

    }
}
