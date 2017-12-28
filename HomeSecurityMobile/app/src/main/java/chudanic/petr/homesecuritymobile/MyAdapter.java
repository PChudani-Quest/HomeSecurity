package chudanic.petr.homesecuritymobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Petr on 16.12.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements ISecurityImagesListener {
    private ArrayList<SecurityImage> imagesList = new ArrayList<>();
    private Context context;

    public MyAdapter(Context context, SecurityImagesProvider imagesProvider) {
        this.context = context;
        imagesProvider.addImagesListener(this);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(imagesList.get(i).getName());
        viewHolder.downloadUrl.setText(imagesList.get(i).getDownloadUrl());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
        viewHolder.timestamp.setText(sdf.format(new Date(imagesList.get(i).getTimestamp())));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SecurityImageActivity.class);
                intent.putExtra("downloadUrl", imagesList.get(i).getDownloadUrl());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    @Override
    public void imagesChanges(Set<SecurityImage> imageSet) {
        imagesList.clear();
        imagesList.addAll(imageSet);

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView downloadUrl;
        private TextView timestamp;
        public ViewHolder(View view) {
            super(view);

            name = (TextView)view.findViewById(R.id.name);
            downloadUrl = (TextView) view.findViewById(R.id.downloadUrl);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
        }
    }
}