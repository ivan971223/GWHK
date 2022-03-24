package com.travel.gwhk.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.R;

public class CardViewHolder  extends RecyclerView.ViewHolder implements  View.OnClickListener {

    public TextView txtCatergoryName;
    public ImageView imageCatView;


    private ItemClickListener itemClickListener;

    public CardViewHolder(View itemView) {
        super(itemView);

        imageCatView = (ImageView) itemView.findViewById(R.id.catergory_icon);
        txtCatergoryName = (TextView)itemView.findViewById(R.id.catergory_name);

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