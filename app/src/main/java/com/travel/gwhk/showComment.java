package com.travel.gwhk;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.travel.gwhk.Common.Common;
import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.Model.Rating;
import com.travel.gwhk.ViewHolder.CommentViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class showComment extends AppCompatActivity {

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference ratingTbl;
    RatingBar ratingBar;
    FirebaseDatabase database;
    String placeId = "";
    SwipeRefreshLayout mSwipeRefreshLayout;

    FirebaseRecyclerAdapter<Rating, CommentViewHolder> adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null)
            adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_dialog);

        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");

        recycler_menu = (RecyclerView)findViewById(R.id.comment_list);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null)
                {
                    placeId = getIntent().getStringExtra(Common.INTENT_PLACE_ID);
                }
                if (!placeId.isEmpty() && placeId != null)
                {

                    Query query = ratingTbl.child(placeId).orderByChild("placeId").equalTo(placeId);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, CommentViewHolder>(options) {

                        @NonNull
                        @Override
                        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.comment_dialog,parent,false);

                            return new CommentViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Rating model) {
                            //holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            Long timeStamp = Long.valueOf(model.getCommentTimeStamp().get("timeStamp").toString());
                            Log.d("timestamp", String.valueOf(timeStamp));
                            holder.txt_comment_date.setText(DateUtils.getRelativeTimeSpanString(timeStamp));

                            holder.ratingBar.setRating(Float.valueOf((float) model.getRateValue()));
                            holder.txtPlaceComment.setText(model.getComment());
                        }
                    };
                    loadComment(placeId);
                }
            }


        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                if (getIntent() != null)
                {
                    placeId = getIntent().getStringExtra(Common.INTENT_PLACE_ID);
                }
                if (!placeId.isEmpty() && placeId != null)
                {

                    Query query = ratingTbl.child(placeId).orderByChild("placeId").equalTo(placeId);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, CommentViewHolder>(options) {

                        @NonNull
                        @Override
                        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.layout_comment,parent,false);

                            return new CommentViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Rating model) {
                            //holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            Long timeStamp = Long.valueOf(model.getCommentTimeStamp().get("timeStamp").toString());

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                            String dateString = formatter.format(new Date(timeStamp));

                            holder.txt_comment_date.setText(dateString);

                            holder.ratingBar.setRating(Float.valueOf((float) model.getRateValue()));

                            holder.rating_score.setText(String.valueOf(model.getRateValue()));

                            holder.txtPlaceComment.setText(model.getComment());

                            holder.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onClick(View view, int position, boolean isLongClick) {

                                }
                            });
                        }
                    };


                    loadComment(placeId);

                }
            }
        });
    }
    private void loadComment(String placeId) {

        adapter.startListening();

        recycler_menu.setAdapter(adapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
