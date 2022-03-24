package com.travel.gwhk.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.R;

public class DetailViewHolder  extends RecyclerView.ViewHolder implements  View.OnClickListener {

    public TextView txtPlaceName;
    public TextView txtPlaceDetail;
    public TextView txtPlaceTime;
    public TextView txtPlaceTransport;
    public TextView txtPlaceFee;
    public ImageView imageView2;
    public ImageButton btn_bookmarks2;

    private ItemClickListener itemClickListener;

    public DetailViewHolder(View itemView) {
        super(itemView);

        imageView2 = (ImageView) itemView.findViewById(R.id.place_image);
        txtPlaceName = (TextView)itemView.findViewById(R.id.place_name);
        txtPlaceDetail = (TextView)itemView.findViewById(R.id.place_detail);
        txtPlaceTime = (TextView)itemView.findViewById(R.id.place_time);
        txtPlaceTransport = (TextView)itemView.findViewById(R.id.place_transportation);
        txtPlaceFee=(TextView)itemView.findViewById(R.id.place_fee);
        btn_bookmarks2=(ImageButton)itemView.findViewById(R.id.btnbookmarks2);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
