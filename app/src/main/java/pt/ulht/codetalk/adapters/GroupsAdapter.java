package pt.ulht.codetalk.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;

import pt.ulht.codetalk.R;
import pt.ulht.codetalk.adapters.FirebaseListAdapter;
import pt.ulht.codetalk.model.AllowedGroup;

/**
 * Created by Joao on 12/07/2014.
 */
public class GroupsAdapter extends FirebaseListAdapter<AllowedGroup> {

    public GroupsAdapter(Query ref, Activity activity, int layout) {
        super(ref, AllowedGroup.class, layout, activity);
    }

    @Override
    protected void populateView(View view, AllowedGroup group) {
        //Map a Group object to an entry in our listview
        String name = group.getName();
        String mode = group.getMode();
        String fullName = group.getFullName();

        TextView tvName = (TextView) view.findViewById(R.id.lvItemGroupName);
        TextView tvMode = (TextView) view.findViewById(R.id.lvItemGroupMode);
        TextView tvFull = (TextView) view.findViewById(R.id.lvItemGroupFullName);

        tvName.setText(name);
        tvMode.setText(mode);
        tvFull.setText(fullName);
    }
}
