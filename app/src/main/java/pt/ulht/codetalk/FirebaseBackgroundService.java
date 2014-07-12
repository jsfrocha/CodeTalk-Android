package pt.ulht.codetalk;


        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.app.Service;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Resources;
        import android.os.IBinder;

        import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;

        public class FirebaseBackgroundService extends Service {

            private Firebase f = new Firebase("https://codetalking.firebaseio.com/groups");
            private ValueEventListener handler;
            private int notificationsSent;

            @Override
            public IBinder onBind(Intent arg0) {
                return null;
            }

            @Override
            public void onCreate() {
                super.onCreate();

                f.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String groupName = dataSnapshot.getName();
                        postNotif("PrevChild: "+s, "Group added: "+groupName.split("_")[0]);
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

                    }
                });
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
