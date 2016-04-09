package com.yc.cepelin.listadapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yc.cepelin.R;
import com.yc.cepelin.model.CafeOverview;

public class CafeOverviewAdapter extends ArrayAdapter<CafeOverview> {

    private ArrayList<CafeOverview> items;
    
    public CafeOverviewAdapter(Context context, int textViewResourceId, ArrayList<CafeOverview> items) {
            super(context, textViewResourceId, items);
            this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	    View v = convertView;
	    ViewHolder holder;
	    
	    if (v == null) {
	        LayoutInflater vi = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        v = vi.inflate(R.layout.cafeoverviewrow, null);
	        
	        holder = new ViewHolder();
	        holder.ttl = (TextView) v.findViewById(R.id.ettl);
	        holder.descr = (TextView) v.findViewById(R.id.edescr);
	        holder.rating = (RatingBar) v.findViewById(R.id.oviewRatingBar);
	        holder.ico = (ImageView) v.findViewById(R.id.distanceicon);
	        holder.eid = (TextView) v.findViewById(R.id.eid);
	        v.setTag(holder);
	    }
	    else {
	    	holder = (ViewHolder) convertView.getTag();
	    }
	
	    CafeOverview co = items.get(position);
	    
	    String imgName="dist1";
	    if (co != null) {
	    	
	    	if (co.getDistance()>=0 && co.getDistance()<=500){
	    		imgName="dist4";
	    	}
	    	else if (co.getDistance()>500 && co.getDistance()<=1000){
	    		imgName="dist3";
	    	}
	    	else if (co.getDistance()>1000 && co.getDistance()<=1500){
	    		imgName="dist2";
	    	}
	    	else if (co.getDistance()>1500){
	    		imgName="dist1";
	    	}
	    	
	    	holder.ttl.setText(co.getTitle());               
	    	holder.descr.setText(co.getShortDescription());
	    	holder.rating.setRating((float) co.getRating());            	
	    	holder.ico.setImageResource(v.getResources().getIdentifier(imgName, "drawable", "com.yc.cepelin"));
	    	holder.eid.setText(String.valueOf(co.getId()));
	    }
	    return v;
    }
    
    static class ViewHolder{
    	TextView ttl = null;
    	TextView descr = null;
    	RatingBar rating = null;
    	TextView dist = null;
    	ImageView ico = null;
    	TextView eid = null;
    }
}