package com.travel.gwhk.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.R;

public class CommentViewHolder  extends RecyclerView.ViewHolder implements  View.OnClickListener {

    public TextView txtPlaceComment, txt_comment_date,rating_score;
    public RatingBar ratingBar;



    private ItemClickListener itemClickListener;

    public CommentViewHolder(View itemView) {
        super(itemView);

        txtPlaceComment = (TextView)itemView.findViewById(R.id.user_comment);
        ratingBar =(RatingBar) itemView.findViewById(R.id.user_rating);
        txt_comment_date= (TextView)itemView.findViewById(R.id.comment_date);
        rating_score = (TextView)itemView.findViewById(R.id.rating_score);

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