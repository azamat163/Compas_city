package com.example.azz.testaltarix;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by azz on 30.08.16.
 */
public class CustomList extends ArrayAdapter<String> {
    private String[] names;
    private String[] metras;
    private Integer[] imageid;
    private Activity context;

    public CustomList(Activity context, String[] names, String[] metras, Integer[] imageid) {
        super(context, R.layout.activity_main_listview, names);
        this.context = context;
        this.names = names;
        this.metras = metras;
        this.imageid = imageid;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_main_listview, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.listview_item_name);
        TextView textViewMetras = (TextView) listViewItem.findViewById(R.id.listview_item_metras);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.listview_image);

        textViewName.setText(names[position]);
        textViewMetras.setText(metras[position]);
        image.setImageResource(imageid[position]);
        return  listViewItem;
    }

}
