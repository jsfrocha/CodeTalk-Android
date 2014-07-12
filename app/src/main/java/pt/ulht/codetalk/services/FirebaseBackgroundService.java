package pt.ulht.codetalk.services;


        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.os.IBinder;
        import android.util.Log;

        import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;

        import pt.ulht.codetalk.R;
        import pt.ulht.codetalk.activities.StartActivity;
        import pt.ulht.codetalk.model.AllowedGroup;

public class FirebaseBackgroundService extends Service {

    private Firebase f;
    private Firebase fTime;
    private double offset;
    private ChildEventListener childListener;
    private int notificationsSent;
    private String SERVICE_TAG = "FirebaseService";
    private String FIREBASE_URL = "https://codetalking.firebaseio.com";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(SERVICE_TAG, "onStartCommand - IN");
        this.f = new Firebase("https://codetalking.firebaseio.com/users/"+intent.getStringExtra("currentUserUid")+"/allowedGroups");
        this.fTime = new Firebase("https://codetalking.firebaseio.com/.info/serverTimeOffset");
        fTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offset = dataSnapshot.getValue(Double.class);
                f.startAt(System.currentTimeMillis() + offset).addChildEventListener(childListener);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        Log.d(SERVICE_TAG, "onStartCommand - OUT");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AllowedGroup ag = dataSnapshot.getValue(AllowedGroup.class);

                postNotif("Group Added!", ag.getName());
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

    private void postNotif(String title, String description) {
        Context mContext = getApplicationContext();
        Intent notificationIntent = new Intent(mContext, StartActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(mContext);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(description);

        Notification n = builder.build();

        mNotificationManager.notify(notificationsSent++, n);

    }

}
