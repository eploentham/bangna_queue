package com.bangna.ekapop.bangna_queue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ekapop on 3/14/2018.
 */

public class ListViewAdapter extends ArrayAdapter<String> {
    private final Context context;
    //        private final String[] values;
    private final ArrayList<String> values;

    public ListViewAdapter(Context context, ArrayList<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View rowView = new View();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView = inflater.inflate(R.layout.listview_layout, parent, false);
//        TextView fLine = (TextView) rowView.findViewById(R.id.fLine);
//        TextView sLine = (TextView) rowView.findViewById(R.id.sLine);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
////            fLine.setText(values[position]);
//        fLine.setText(values.get(position));
//        // change the icon for Windows and iPhone
////            String s = values[position];
//        String s = values.get(position);
//        if (s.startsWith("iPhone")) {
//            imageView.setImageResource(R.drawable.idel_blue);
//        } else {
//            imageView.setImageResource(R.drawable.idel_red);
//        }
//        imageView.setVisibility(View.INVISIBLE);
        return null;
    }
}
