package com.ramstrenconsultingllc.cdlr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowCardAdapter extends ArrayAdapter<ShowCard> {
    int layoutResourceId;
    ShowCard data[] = null;
    Context context;

    public ShowCardAdapter(Context context, int layoutResourceId, ShowCard[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ShowCardHolder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ShowCardHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.currentBid = (TextView)row.findViewById(R.id.pricing);
            holder.yourPlace = (CheckBox)row.findViewById(R.id.YourPlaceCheckbox);
            holder.hotel = (CheckBox)row.findViewById(R.id.HotelCheckbox);
            holder.serviceLength = (TextView)row.findViewById(R.id.TimeText);
            holder.timeFrame = (TextView)row.findViewById(R.id.timeFrame);
            holder.bidTimeLeft = (TextView)row.findViewById(R.id.BidTimeLeftText);
            holder.bidderCount = (TextView)row.findViewById(R.id.BiddersCountText);
            holder.ratingCount = (TextView)row.findViewById(R.id.RatingText);
            holder.activeName = (TextView)row.findViewById(R.id.activeName);

            row.setTag(holder);
        }
        else
        {
            holder = (ShowCardHolder)row.getTag();
        }

        ShowCard showcard = data[position];
        holder.imgIcon.setImageResource(showcard.icon);
        holder.currentBid.setText(showcard.currentBid);
        holder.yourPlace.setChecked(showcard.yourPlace);
        holder.hotel.setChecked(showcard.hotel);
        holder.serviceLength.setText(showcard.serviceLength);
        holder.timeFrame.setText(showcard.timeFrame);
        holder.bidTimeLeft.setText(showcard.bidTimeLeft);
        holder.bidderCount.setText(showcard.bidderCount);
        holder.ratingCount.setText(showcard.ratingCount);
        holder.activeName.setText(showcard.activeName);

        return row;
    }

    static class ShowCardHolder
    {
        ImageView imgIcon;
        TextView currentBid;
        TextView activeName;
        CheckBox yourPlace;
        CheckBox hotel;
        TextView serviceLength;
        TextView timeFrame;
        TextView bidTimeLeft;
        TextView bidderCount;
        TextView ratingCount;
    }

}
