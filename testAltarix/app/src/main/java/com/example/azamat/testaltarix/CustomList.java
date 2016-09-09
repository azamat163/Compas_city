package com.example.azamat.testaltarix;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by azamat on 31.08.16.
 */
public class CustomList extends RecyclerView.Adapter<CustomList.geosearchViewHolder> {

  private  List<geosearch> geosearches;

    CustomList(List<geosearch> geosearches){
        this.geosearches = geosearches;
    }

    @Override
    public geosearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_cardview, viewGroup, false);
        geosearchViewHolder pvh = new geosearchViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(CustomList.geosearchViewHolder holder, int i) {
        holder.title.setText(geosearches.get(i).title);
        holder.dist.setText(String.valueOf(geosearches.get(i).dist));
        holder.image.setImageResource(geosearches.get(i).image);
    }


    @Override
    public int getItemCount() {
        if (geosearches != null) {
            return geosearches.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static  class geosearchViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView dist;
        ImageView image;

        geosearchViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            dist = (TextView)itemView.findViewById(R.id.dist);
            image = (ImageView)itemView.findViewById(R.id.image);
        }
    }
}
