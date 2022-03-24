package com.travel.gwhk.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travel.gwhk.Database.Database;
import com.travel.gwhk.Fragment.BookmarkFragment;
import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.Model.Bookmark;
import com.travel.gwhk.R;
import com.travel.gwhk.ViewHolder.BookmarkViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkViewHolder> {

    private Context context;
    private List<Bookmark> bookmarkList = new ArrayList<>();
    DatabaseReference Place;
    Database localDB;

    public BookmarkAdapter( List<Bookmark> bookmarkList,Context context) {
        this.bookmarkList = bookmarkList;
        this.context = context;

    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.bookmark,parent,false);
        Place=FirebaseDatabase.getInstance().getReference("Place");

        return new BookmarkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkViewHolder holder, final int position) {
        holder.txtPlaceCategory.setText(bookmarkList.get(position).getPlaceCategory());
        holder.txtPlaceName.setText(bookmarkList.get(position).getPlaceName());
        holder.txtPlaceDistrict.setText(bookmarkList.get(position).getPlaceDistrict());
        holder.txtPlaceRegion.setText(bookmarkList.get(position).getPlaceRegion());
        holder.txtPlaceDescription.setText(bookmarkList.get(position).getPlaceDescription());
        holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);

        Query query = Place.child(bookmarkList.get(position).getCategoryId()).child(bookmarkList.get(position).getPlaceId());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("district", String.valueOf(dataSnapshot.child("district")));
               //double ratingvalue = dataSnapshot.child("ratingValue").getValue(double.class);
               //holder.rating_score.setText(String.valueOf(ratingvalue));
               //holder.ratingBar.setRating((float)ratingvalue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //holder.ratingBar.setRating((float) ratingValue);

        Picasso.with(context).load(bookmarkList.get(position).getPlaceImage()).into(holder.imageView1);

        localDB = new Database(context);

        final Bookmark clickItem = bookmarkList.get(position);


        holder.btn_bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { {

                localDB.removeFromBookmark(bookmarkList.get(position).getPlaceId(),bookmarkList.get(position).getCategoryId());
                holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                Toast.makeText(context, "已取消書籤", Toast.LENGTH_LONG).show();
                ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.container, new BookmarkFragment()).commit();
            }



            }

        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent placeDetail= new Intent(context, com.travel.gwhk.placeDetail.class);
                placeDetail.putExtra("PlaceId", bookmarkList.get(position).getPlaceId());//send drinks id to new activity
                placeDetail.putExtra("CategoryId", bookmarkList.get(position).getCategoryId());
                context.startActivity(placeDetail);
            }
        });


    }


    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public void removeItem(int position)
    {
        bookmarkList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Bookmark item, int position)
    {
        bookmarkList.add(position,item);
        notifyItemInserted(position);


    }

    public Bookmark getitem (int position)
    {
        return bookmarkList.get(position);
    }
}
