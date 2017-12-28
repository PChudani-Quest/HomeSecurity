package chudanic.petr.homesecuritymobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IStatusListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        securitySwitch = (Switch) findViewById(R.id.securitySwitch);
        mainContent = findViewById(R.id.mainContent);
        statusSwitchProgress = findViewById(R.id.statusSwitchProgress);
        statusText = findViewById(R.id.statusText);
        securityImages = findViewById(R.id.securityImages);

        securitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                showProgress(true);

                firebase.startStop(b, new IFunction<ControlRequest, Void>() {
                    @Override
                    public Void call(ControlRequest param) {
                        firebase.requestStatus();
                        showProgress(false);
                        return null;
                    }
                });

            }
        });

        securityImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecurityImagesActivity.class);
                startActivity(intent);
            }
        });

        firebase = DeviceController.getInstance();
        firebase.addStatusListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebase.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    Switch securitySwitch;
    View mainContent;
    View statusSwitchProgress;
    TextView statusText;
    Button securityImages;
    DeviceController firebase;

    /**
     * Shows the progress UI and hides the controls.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainContent.setVisibility(show ? View.GONE : View.VISIBLE);
            mainContent.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainContent.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            statusSwitchProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            statusSwitchProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    statusSwitchProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            statusSwitchProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mainContent.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void statusChanged(Status newStatus) {
        statusText.setText(newStatus.getStatus());

        if (newStatus.getStatus().equals("RUNNING")) {
            securitySwitch.setChecked(true);
        }
        if (newStatus.getStatus().equals("STOPPED")) {
            securitySwitch.setChecked(false);
        }
    }
}
