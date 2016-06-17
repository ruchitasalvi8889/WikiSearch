package com.example.wikiimagesearch;


import java.lang.reflect.Array;
import java.util.ArrayList;

import com.androidquery.AQuery;
import com.example.wikiimagesearch.imagehelpers.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
 
public class CustomGridAdapter extends BaseAdapter{
      private Context mContext;
      ArrayList<ThumbNail> mList=new ArrayList<>();
      public ImageLoader imageLoader; 
        public CustomGridAdapter(Context c,ArrayList<ThumbNail>list) {
            mContext = c;
            this.mList=list;
            imageLoader=new ImageLoader(mContext);
        }
 
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }
 
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mList.get(position);
        }
 
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            if (convertView == null) {
 
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.single_grid, null);
                ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
                if(mList.get(position).source!=null)
                {
//                	imageView.getLayoutParams().height = mList.get(position).getHeight();
//                	imageView.getLayoutParams().width = mList.get(position).getWidth();
                	imageView.requestLayout();
                	imageLoader.DisplayImage(mList.get(position).source, imageView);
                	
                }
            } else {
                grid = (View) convertView;
            }
 
            return grid;
        }
}