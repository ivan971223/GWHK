package com.travel.gwhk.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.R;

public class PlaceViewHolder  extends RecyclerView.ViewHolder implements  View.OnClickListener {

    public TextView txtPlaceName;
    public TextView txtPlaceDescription;
    public TextView txtPlaceRegion;
    public TextView txtPlaceDistrict;
    public TextView txtPlaceCategory;
    public ImageView imageView1;
    public ImageButton btn_bookmarks;
    public TextView rating_score;
    public RatingBar ratingBar;

    private ItemClickListener itemClickListener;

    public PlaceViewHolder(View itemView) {
        super(itemView);

        imageView1 = (ImageView) itemView.findViewById(R.id.place_image);
        txtPlaceName = (TextView)itemView.findViewById(R.id.place_name);
        txtPlaceDescription = (TextView)itemView.findViewById(R.id.place_description);
        txtPlaceDistrict = (TextView)itemView.findViewById(R.id.place_district);
        txtPlaceRegion = (TextView)itemView.findViewById(R.id.place_region);
        txtPlaceCategory=(TextView)itemView.findViewById(R.id.place_category);
        btn_bookmarks=(ImageButton)itemView.findViewById(R.id.btn_bookmark);
        rating_score = (TextView)itemView.findViewById(R.id.rating_score);
        ratingBar =(RatingBar) itemView.findViewById(R.id.rating_bar);
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