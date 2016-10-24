package com.example.azamat.testaltarix;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


/**
 * Created by azamat on 31.08.16.
 */
public class CustomGeoListAdapter extends RecyclerView.Adapter<CustomGeoListAdapter.geosearchViewHolder> {

    private static ArrayList<GeoDataModel> GeoDataModels;
    private Context mContext;


  public  CustomGeoListAdapter(Context mContext,ArrayList<GeoDataModel> GeoDataModels){
        this.GeoDataModels = GeoDataModels;
        this.mContext = mContext;
    }

    @Override
    public geosearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.activity_cardview, viewGroup, false);
        geosearchViewHolder pvh = new geosearchViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(CustomGeoListAdapter.geosearchViewHolder holder, int i) {
        GeoDataModel geo = GeoDataModels.get(i);
        holder.title.setText(GeoDataModels.get(i).getTitle());
        String plural = mContext.getResources().getQuantityString(R.plurals.plurals,GeoDataModels.get(i).getDist(),GeoDataModels.get(i).getDist());
        holder.dist.setText(String.valueOf(GeoDataModels.get(i).getDist()));
        holder.dist.setText(plural);
        Glide.with(mContext)
                .load(geo.getImage())
                .error(R.drawable.icon)
                .override(60, 60)
                .centerCrop()
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
        if (GeoDataModels != null) {
            return GeoDataModels.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static  class geosearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cv;
        private TextView title;
        private  TextView dist;
        private ImageView image;
        private Context context;


        geosearchViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            dist = (TextView)itemView.findViewById(R.id.dist);
            image = (ImageView)itemView.findViewById(R.id.image);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context,WebViewItemActivity.class);
            intent.putExtra("item", GeoDataModels.get(position).getTitle());
            context.startActivity(intent);
        }
    }
}
