package com.example.jy.scv;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{

    private ArrayList<ListV0> listV0 = new ArrayList<ListV0>();
    public ListViewAdapter(){

    }

    @Override
    public int getCount() {
        return listV0.size();
    }

    @Override
    public Object getItem(int position) {
        return listV0.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview,parent,false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.img);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView Context = (TextView) convertView.findViewById(R.id.context);

        ListV0 listViewItem = listV0.get(position);

        image.setImageDrawable(listViewItem.getImg());
        title.setText(listViewItem.getTitle());
        Context.setText(listViewItem.getContext());


        return convertView;
    }

    public void addData(Drawable icon, String title, String desc){
        ListV0 item = new ListV0();

        item.setImg(icon);
        item.setTitle(title);
        item.setContext(desc);

        listV0.add(item);
    }

}
