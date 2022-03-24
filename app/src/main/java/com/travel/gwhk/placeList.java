package com.travel.gwhk;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travel.gwhk.Database.Database;
import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.Model.Bookmark;
import com.travel.gwhk.Model.Category;
import com.travel.gwhk.Model.Place;
import com.travel.gwhk.ViewHolder.PlaceViewHolder;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class placeList extends AppCompatActivity {
    EditText mSearchField;
    ImageButton mSearchBtn;
    ImageButton btnBookmarks;

    String categoryId = "";

    DatabaseReference place;
    DatabaseReference category;
    FirebaseDatabase database;
    float average;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference ratingTbl;
    RatingBar ratingBar;
    Database localDB;

    Category currentCategory;

    FirebaseRecyclerAdapter<Place, PlaceViewHolder> adapter;
    FirebaseRecyclerAdapter<Place, PlaceViewHolder> filterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance();
        place = database.getReference("Place");
        category = database.getReference("Category");

        localDB = new Database(this);

        ratingTbl = database.getReference("Rating");
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);
        btnBookmarks = (ImageButton) findViewById(R.id.btn_bookmark);

        final Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(placeList.this, MainActivity.class));
            }
        });
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);


        recycler_menu = (RecyclerView) findViewById(R.id.place_list);

        recycler_menu.setHasFixedSize(true);

        recycler_menu.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        Query query = category.child(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentCategory = dataSnapshot.getValue(Category.class);
                toolbar.setTitle(currentCategory.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!categoryId.isEmpty() && categoryId != null) {
            loadListPlace(categoryId);
        }


        NiceSpinner spinner_left = (NiceSpinner) findViewById(R.id.place_spinner_left);
        final NiceSpinner spinner_right = (NiceSpinner) findViewById(R.id.place_spinner_right);
        final List<String> dataset = new LinkedList<>(Arrays.asList("", "港島", "九龍", "新界"));


        spinner_left.attachDataSource(dataset);


        spinner_left.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String region = (String) parent.getItemAtPosition(position);

                switch (region) {
                    case "港島":
                        List<String> dataset1 = new LinkedList<>(Arrays.asList("", "中西區", "東區", "南區", "灣仔區"));
                        spinner_right.attachDataSource(dataset1);

                        break;
                    case "九龍":
                        List<String> dataset2 = new LinkedList<>(Arrays.asList("", "九龍城區", "觀塘區", "深水埗區", "黃大仙區", "油尖旺區"));
                        spinner_right.attachDataSource(dataset2);
                        break;
                    case "新界":
                        List<String> dataset3 = new LinkedList<>(Arrays.asList("", "離島區", "葵青區", "北區", "西貢區", "沙田區", "大埔區", "荃灣區", "屯門區", "元朗區"));
                        spinner_right.attachDataSource(dataset3);
                        break;


                }
                setPlaceList(region);

            }

            private void setPlaceList(final String region) {
                final Query filtered_place = place.child(categoryId).orderByChild("region").equalTo(region);

                                FirebaseRecyclerOptions<Place> options = new FirebaseRecyclerOptions.Builder<Place>()
                                        .setQuery(filtered_place, Place.class).build();

                                filterAdapter = new FirebaseRecyclerAdapter<Place, PlaceViewHolder>(options) {


                                    @NonNull
                                    @Override
                                    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place, parent, false);
                                        return new PlaceViewHolder(itemView);
                                    }

                                    @Override
                                    protected void onBindViewHolder(@NonNull final PlaceViewHolder holder, final int position, @NonNull final Place model) {
                                        holder.txtPlaceCategory.setText(model.getCategory());
                                        holder.txtPlaceName.setText(model.getName());
                                        holder.txtPlaceDistrict.setText(model.getDistrict());
                                        holder.txtPlaceRegion.setText(model.getRegion());
                                        holder.txtPlaceDescription.setText(model.getDescription());

                                        double ratingValue = model.getRatingValue();
                                        ratingValue = Math.round(ratingValue * 10.0) / 10.0;


                                        holder.rating_score.setText(String.valueOf(ratingValue));
                                        holder.ratingBar.setRating((float) ratingValue);

                                        Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imageView1);

                                        final Place clickItem = model;

                                        if (localDB.isBookmark(filterAdapter.getRef(position).getKey(),categoryId))
                                            holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);

                                        holder.btn_bookmarks.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Bookmark bookmark = new Bookmark();
                                                bookmark.setPlaceName(model.getName());
                                                bookmark.setPlaceId(filterAdapter.getRef(position).getKey());
                                                bookmark.setPlaceCategory(model.getCategory());
                                                bookmark.setPlaceDescription(model.getDescription());
                                                bookmark.setPlaceDistrict(model.getDistrict());
                                                bookmark.setPlaceRegion(model.getRegion());
                                                bookmark.setPlaceImage(model.getImage());
                                                bookmark.setCategoryId(categoryId);


                                                if (!localDB.isBookmark(filterAdapter.getRef(position).getKey(),categoryId)) {
                                                    localDB.addToBookmark(bookmark);
                                                    holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                                    Toast.makeText(placeList.this, "已加入書籤", Toast.LENGTH_LONG).show();

                                                } else {
                                                    localDB.removeFromBookmark(filterAdapter.getRef(position).getKey(),categoryId);
                                                    holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                                                    Toast.makeText(placeList.this, "已取消書籤", Toast.LENGTH_LONG).show();

                                                }


                                            }
                                        });

                                        holder.setItemClickListener(new ItemClickListener() {
                                            @Override
                                            public void onClick(View view, int position, boolean isLongClick) {
                                                Intent placeDetail = new Intent(placeList.this, com.travel.gwhk.placeDetail.class);
                                                placeDetail.putExtra("PlaceId", filterAdapter.getRef(position).getKey());
                                                placeDetail.putExtra("CategoryId", categoryId);//send drinks id to new activity
                                                startActivity(placeDetail);
                                            }
                                        });

                                    }
                                };
                                filterAdapter.startListening();
                                recycler_menu.setAdapter(filterAdapter);
                            }



                });

        spinner_right.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                String district = (String) parent.getItemAtPosition(position);

                setPlaceList(district);

            }

            private void setPlaceList(final String district) {

                final Query filtered_place = place.child(categoryId).orderByChild("district").equalTo(district);

                FirebaseRecyclerOptions<Place> options = new FirebaseRecyclerOptions.Builder<Place>()
                        .setQuery(filtered_place, Place.class).build();

                filterAdapter = new FirebaseRecyclerAdapter<Place, PlaceViewHolder>(options) {


                    @NonNull
                    @Override
                    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place, parent, false);
                        return new PlaceViewHolder(itemView);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final PlaceViewHolder holder, final int position, @NonNull final Place model) {
                        holder.txtPlaceCategory.setText(model.getCategory());
                        holder.txtPlaceName.setText(model.getName());
                        holder.txtPlaceDistrict.setText(model.getDistrict());
                        holder.txtPlaceRegion.setText(model.getRegion());
                        holder.txtPlaceDescription.setText(model.getDescription());

                        double ratingValue = model.getRatingValue();
                        ratingValue = Math.round(ratingValue * 10.0) / 10.0;


                        holder.rating_score.setText(String.valueOf(ratingValue));
                        holder.ratingBar.setRating((float) ratingValue);

                        Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imageView1);

                        final Place clickItem = model;

                        if (localDB.isBookmark(filterAdapter.getRef(position).getKey(),categoryId))
                            holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);

                        holder.btn_bookmarks.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Bookmark bookmark = new Bookmark();
                                bookmark.setPlaceName(model.getName());
                                bookmark.setPlaceId(filterAdapter.getRef(position).getKey());
                                bookmark.setPlaceCategory(model.getCategory());
                                bookmark.setPlaceDescription(model.getDescription());
                                bookmark.setPlaceDistrict(model.getDistrict());
                                bookmark.setPlaceRegion(model.getRegion());
                                bookmark.setPlaceImage(model.getImage());
                                bookmark.setCategoryId(categoryId);


                                if (!localDB.isBookmark(filterAdapter.getRef(position).getKey(),categoryId)) {
                                    localDB.addToBookmark(bookmark);
                                    holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                    Toast.makeText(placeList.this, "已加入書籤", Toast.LENGTH_LONG).show();

                                } else {
                                    localDB.removeFromBookmark(filterAdapter.getRef(position).getKey(),categoryId);
                                    holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                                    Toast.makeText(placeList.this, "已取消書籤", Toast.LENGTH_LONG).show();

                                }


                            }
                        });

                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Intent placeDetail = new Intent(placeList.this, com.travel.gwhk.placeDetail.class);
                                placeDetail.putExtra("PlaceId", filterAdapter.getRef(position).getKey());//send drinks id to new activity
                                placeDetail.putExtra("CategoryId", categoryId);
                                startActivity(placeDetail);
                            }
                        });

                    }
                };
                filterAdapter.startListening();
                recycler_menu.setAdapter(filterAdapter);
            }
        });
    }



    private void loadListPlace(final String categoryId) {

                FirebaseRecyclerOptions<Place> options =
                        new FirebaseRecyclerOptions.Builder<Place>().setQuery(place.child(categoryId), Place.class).setLifecycleOwner(this).build();

                adapter = new FirebaseRecyclerAdapter<Place, PlaceViewHolder>(options) {


                    @Override
                    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place, parent, false);
                        return new PlaceViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final PlaceViewHolder holder, final int position, @NonNull final Place model) {
                        holder.txtPlaceCategory.setText(model.getCategory());
                        holder.txtPlaceName.setText(model.getName());
                        holder.txtPlaceDistrict.setText(model.getDistrict());
                        holder.txtPlaceRegion.setText(model.getRegion());
                        holder.txtPlaceDescription.setText(model.getDescription());

                        double ratingValue = model.getRatingValue();
                        ratingValue = Math.round(ratingValue * 10.0) / 10.0;


                        holder.rating_score.setText(String.valueOf(ratingValue));
                        holder.ratingBar.setRating((float) ratingValue);

                        Picasso.with(placeList.this.getBaseContext()).load(model.getImage()).into(holder.imageView1);

                        final Place clickItem = model;

                        if (localDB.isBookmark(adapter.getRef(position).getKey(),categoryId))
                            holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);

                        holder.btn_bookmarks.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Bookmark bookmark = new Bookmark();
                                bookmark.setPlaceName(model.getName());
                                bookmark.setPlaceId(adapter.getRef(position).getKey());
                                bookmark.setPlaceCategory(model.getCategory());
                                bookmark.setPlaceDescription(model.getDescription());
                                bookmark.setPlaceDistrict(model.getDistrict());
                                bookmark.setPlaceRegion(model.getRegion());
                                bookmark.setPlaceImage(model.getImage());
                                bookmark.setCategoryId(categoryId);


                                if (!localDB.isBookmark(adapter.getRef(position).getKey(),categoryId)) {
                                    localDB.addToBookmark(bookmark);
                                    holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                    Toast.makeText(placeList.this, "已加入書籤", Toast.LENGTH_LONG).show();

                                } else {
                                    localDB.removeFromBookmark(adapter.getRef(position).getKey(),categoryId);
                                    holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                                    Toast.makeText(placeList.this, "已取消書籤", Toast.LENGTH_LONG).show();

                                }


                            }
                        });

                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Intent placeDetail = new Intent(placeList.this, com.travel.gwhk.placeDetail.class);
                                placeDetail.putExtra("PlaceId", adapter.getRef(position).getKey());//send drinks id to new activity
                                placeDetail.putExtra("CategoryId", categoryId);
                                startActivity(placeDetail);
                            }
                        });


                    }
                };

                recycler_menu.setAdapter(adapter);
            }

    @Override
    protected void onStop() {
        if (adapter!=null)
            adapter.stopListening();
        if (filterAdapter!=null)
            filterAdapter.stopListening();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }


}