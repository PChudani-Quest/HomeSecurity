package chudanic.petr.homesecuritymobile;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SecurityImagesActivity extends AppCompatActivity {
    private SecurityImagesProvider imagesProvider;

    @Override
    protected void onStart() {
        super.onStart();
        imagesProvider.startListenForImagesData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        imagesProvider.stopListenForImagesData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_images);

        imagesProvider = SecurityImagesProvider.getInstance();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.securityimages);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SecurityImagesActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);
        MyAdapter adapter = new MyAdapter(SecurityImagesActivity.this, imagesProvider);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
