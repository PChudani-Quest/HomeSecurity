package chudanic.petr.homesecuritymobile;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import static android.content.ContentValues.TAG;

/**
 * Created by Petr on 16.12.2017.
 */

public class SecurityImagesProvider {

    private static SecurityImagesProvider instance;
    private List<ISecurityImagesListener> imagesListeners = new ArrayList<>();
    private Set<SecurityImage> securityImagesSet = new TreeSet<>(new Comparator<SecurityImage>() {
        @Override
        public int compare(SecurityImage i1, SecurityImage i2) {
            return (int)(i2.getTimestamp() - i1.getTimestamp());
        }
    });

    ChildEventListener securityImagesListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            SecurityImage image = dataSnapshot.getValue(SecurityImage.class);
            securityImagesSet.add(image);
            notifyImageListeners();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            SecurityImage image = dataSnapshot.getValue(SecurityImage.class);
            securityImagesSet.remove(image);
            notifyImageListeners();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void addImagesListener(ISecurityImagesListener l) {
        this.imagesListeners.add(l);
    }

    public void removeImagesListener(ISecurityImagesListener l) {
        this.imagesListeners.remove(l);
    }

    private void notifyImageListeners() {
        for (ISecurityImagesListener l: imagesListeners.toArray(new ISecurityImagesListener[0])) {
            l.imagesChanges(SecurityImagesProvider.this.securityImagesSet);
        }
    }

    private SecurityImagesProvider() {

    }

    public static SecurityImagesProvider getInstance() {
        if (instance == null) {
            instance = new SecurityImagesProvider();
        }
        return instance;
    }

    public void stopListenForImagesData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference securityImages = database.getReference("security_images");

        securityImages.removeEventListener(securityImagesListener);
    }

    public void startListenForImagesData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference securityImages = database.getReference("security_images");

        securityImages.addChildEventListener(securityImagesListener);
    }
}
