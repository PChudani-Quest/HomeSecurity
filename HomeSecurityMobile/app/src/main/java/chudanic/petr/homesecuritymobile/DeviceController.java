package chudanic.petr.homesecuritymobile;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by Petr on 10.12.2017.
 */

public class DeviceController {

    private static DeviceController instance;
    private Status status;
    private List<IStatusListener> statusListeners = new ArrayList<>();
    Timer timer;



    public void addStatusListener(IStatusListener l) {
        this.statusListeners.add(l);
    }

    public void removeStatusListener(IStatusListener l) {
        this.statusListeners.remove(l);
    }

    private void notifyStatusListeners() {
        for (IStatusListener l: statusListeners.toArray(new IStatusListener[0])) {
            l.statusChanged(DeviceController.this.status);
        }
    }

    private DeviceController() {

    }
    
    public static DeviceController getInstance() {
        if (instance == null) {
            instance = new DeviceController();
        }
        return instance;
    }

    public void start() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                requestStatus();
            }
        };
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 60*1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void requestStatus() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference statusRequests = database.getReference("status_requests");

        final DatabaseReference newRequest = statusRequests.push();
        newRequest.setValue(new Status());

        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Status value = dataSnapshot.getValue(Status.class);
                Log.d(TAG, "Status is: " + value.getStatus());

                if (value.isProcessed()) {
                    newRequest.removeEventListener(this);
                    newRequest.removeValue();
                    DeviceController.this.status = value;
                    notifyStatusListeners();
                } else if(isStatusNotResponding(value)) {
                    value.setStatus("NOT RESPONDING");
                    DeviceController.this.status = value;
                    notifyStatusListeners();
                } else {
                    // We're waiting for status to be processed
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        // Listen for new request changes
        newRequest.addValueEventListener(listener);
    }

    private boolean isStatusNotResponding(Status value) {
        if (new Date().getTime() - value.getDate() > 5000l) {
            return true;
        }
        return false;
    }

    public void startStop(boolean start, final IFunction<ControlRequest, Void> callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference controlRequests = database.getReference("control_requests");

        final DatabaseReference newRequest = controlRequests.push();
        ControlRequest req = new ControlRequest();
        if (start) {
            req.setAction("START");
        } else {
            req.setAction("STOP");
        }
        newRequest.setValue(req);

        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ControlRequest value = dataSnapshot.getValue(ControlRequest.class);
                Log.d(TAG, "Control request is processed: " + value.isProcessed());

                if (value.isProcessed()) {
                    callback.call(value);
                    newRequest.removeEventListener(this);
                    newRequest.removeValue();
                } else {
                    // We're waiting for status to be processed
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        // Listen for new request changes
        newRequest.addValueEventListener(listener);
    }
}
