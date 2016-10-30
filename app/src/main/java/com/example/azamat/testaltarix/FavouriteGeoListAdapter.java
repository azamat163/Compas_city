package com.example.azamat.testaltarix;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.azamat.testaltarix.db.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by azamat on 30.10.16.
 */

public class FavouriteGeoListAdapter extends RecyclerView.Adapter<FavouriteGeoListAdapter.geofavouriteViewHolder> {
    private static ArrayList<GeoDataModel> GeoDataModels;
    private Context mContext;
    private DatabaseHandler db;

    public  FavouriteGeoListAdapter(Context mContext, ArrayList<GeoDataModel> GeoDataModels){
        this.GeoDataModels = GeoDataModels;
        this.mContext = mContext;
    }
    @Override
    public FavouriteGeoListAdapter.geofavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.activity_cardview_favourite, parent, false);
        FavouriteGeoListAdapter.geofavouriteViewHolder pvh = new FavouriteGeoListAdapter.geofavouriteViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(FavouriteGeoListAdapter.geofavouriteViewHolder holder, int i) {
        final GeoDataModel geo = GeoDataModels.get(i);
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

    public static  class geofavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cv;
        private TextView title;
        private  TextView dist;
        private ImageView image;
        private Context context;
        private ImageButton imageButton;


        geofavouriteViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
            cv = (CardView)itemView.findViewById(R.id.cv_fav);
            title = (TextView)itemView.findViewById(R.id.title_fav);
            dist = (TextView)itemView.findViewById(R.id.dist_fav);
            image = (ImageView)itemView.findViewById(R.id.image_fav);
            imageButton = (ImageButton)itemView.findViewById(R.id.favourite_fav);
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
