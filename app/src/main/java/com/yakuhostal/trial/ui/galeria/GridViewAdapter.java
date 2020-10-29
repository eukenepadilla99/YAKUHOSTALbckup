package com.yakuhostal.trial.ui.galeria;

import android.content.Context;
import com.yakuhostal.trial.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class GridViewAdapter extends BaseAdapter {

    private  Context context;
    private  String [] items;

    public GridViewAdapter (Context context, String[] items){
        super();
        this.context=context;
        this.items=items;
    }

    @Override
    public int getCount() {
        //Numero de elementos que contiene nuestro array
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView img;
        if(convertView==null){
            img=new ImageView(context);
            convertView=img;
            img.setPadding(5,5,5,5);
        }else{
            img=(ImageView)convertView;
        }
        Picasso.get()
                .load(items[position])
                .placeholder(R.drawable.picture)
                .resize(500,500)
                .into(img);
        return convertView;
    }
}
