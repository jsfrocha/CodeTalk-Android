package pt.ulht.codetalk.services;


        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.IBinder;
        import android.util.Log;

        import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;

        import java.util.Set;

        import pt.ulht.codetalk.CodeTalk;
        import pt.ulht.codetalk.R;
        import pt.ulht.codetalk.activities.GroupActivity;
        import pt.ulht.codetalk.activities.StartActivity;
        import pt.ulht.codetalk.model.AllowedGroup;
        import pt.ulht.codetalk.model.Note;

public class FirebaseBackgroundService extends Service {

    private Firebase f;
    private ChildEventListener childListener;
    private int notificationsSent;
    private String SERVICE_TAG = "FirebaseService";
    private String FIREBASE_URL = "https://codetalking.firebaseio.com";
    private CodeTalk app;
    private Firebase groupRef;
    private SharedPreferences mShared;
    private Set<String> followedGroups;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(SERVICE_TAG, "onStartCommand - IN");
        //this.f = new Firebase("https://codetalking.firebaseio.com/users/"+intent.getStringExtra("currentUserUid")+"/allowedGroups");

        //f.endAt().limit(1).addChildEventListener(childListener);

        followedGroups = mShared.getStringSet("followedGroups", null);

        if (followedGroups != null) {
            for (String group : followedGroups) {
                Log.d(SERVICE_TAG, "Service Listen Group: " + group);
                groupRef = new Firebase("https://codetalking.firebaseio.com/groups/" + group + "/notes");
                groupRef.endAt().limit(1).addChildEventListener(childListener);
            }
        }

        Log.d(SERVICE_TAG, "onStartCommand - OUT");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(SERVICE_TAG, "onCreate IN");
        mShared = getSharedPreferences("pt.ulht.codetalk", Context.MODE_PRIVATE);
        this.childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Note n = dataSnapshot.getValue(Note.class);
                String title = "New Note in "+n.getInGroup().split("_")[0]+"!";
                String description = n.getContent();
                postNotif(title, description, n.getInGroup());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("FBERROR", firebaseError.toString());
            }
        };

        Log.d(SERVICE_TAG, "onCreate - OUT");
    }

    private void postNotif(String title, String description, String intentDestination) {
        Log.d("POSTNOTIF", intentDestination);
        Context mContext = getApplicationContext();
        Intent notificationIntent = new Intent(mContext, GroupActivity.class);
        notificationIntent.putExtra("groupName", intentDestination);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true);

        Notification n = builder.build();

        mNotificationManager.notify(notificationsSent++, n);

    }

}
