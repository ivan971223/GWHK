package com.travel.gwhk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.travel.gwhk.Database.Database;
import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.Model.Bookmark;
import com.travel.gwhk.Model.Place;
import com.travel.gwhk.ViewHolder.PlaceViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    FirebaseRecyclerAdapter<Place, PlaceViewHolder> adapter;
    FirebaseRecyclerAdapter<Place, PlaceViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    DatabaseReference place;
    DatabaseReference category;
    FirebaseDatabase database;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    CharSequence searchText;

    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        database = FirebaseDatabase.getInstance();
        place = database.getReference("Place");
        category=database.getReference("Category");

        localDB = new Database(this);

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_search);
        recycler_menu.setHasFixedSize(true);
        recycler_menu.setLayoutManager(new LinearLayoutManager(this));

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);

        materialSearchBar.setHint("尋找香港景點");
        materialSearchBar.setSpeechMode(false);
        materialSearchBar.openSearch();
        materialSearchBar.setRoundedSearchBarEnabled(true);

        loadSuggest();


        if (getIntent() != null) {
            searchText = getIntent().getStringExtra("searchText");
                //Log.d("searchText", String.valueOf(searchText));
            materialSearchBar.setText(String.valueOf(searchText));
            startSearchx(searchText);

        }



        materialSearchBar.setCardViewElevation(20);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);

                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //enable searchbar callbacks
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recycler_menu.setAdapter(searchAdapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    startSearchx(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {


            }
        });
        //restore last queries from disk

        /*
        materialSearchBar.setLastSuggestions(list);
        //Inflate menu and setup OnMenuItemClickListener
        materialSearchBar.inflateMenu(R.menu.main);
        materialSearchBar.getMenu().setOnMenuItemClickListener(this);
        */

    }


    private void startSearchx(CharSequence text) {

        searchAdapter = null;

        for (int id = 1; ((searchAdapter == null)&& (id<20)); id++) {

            final String categoryId = "0" + id;

            Log.d("Cid",categoryId);

            final Query searchByName = place.child(categoryId).orderByChild("name").startAt(String.valueOf(text)).endAt(String.valueOf(text) + "\uf8ff");

            searchByName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        FirebaseRecyclerOptions<Place> options = new FirebaseRecyclerOptions.Builder<Place>()
                                .setQuery(searchByName, Place.class).build();

                        searchAdapter = new FirebaseRecyclerAdapter<Place, PlaceViewHolder>(options) {


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

                                if (localDB.isBookmark(searchAdapter.getRef(position).getKey(),categoryId))
                                    holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);

                                holder.btn_bookmarks.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Bookmark bookmark = new Bookmark();
                                        bookmark.setPlaceName(model.getName());
                                        bookmark.setPlaceId(searchAdapter.getRef(position).getKey());
                                        bookmark.setPlaceCategory(model.getCategory());
                                        bookmark.setPlaceDescription(model.getDescription());
                                        bookmark.setPlaceDistrict(model.getDistrict());
                                        bookmark.setPlaceRegion(model.getRegion());
                                        bookmark.setPlaceImage(model.getImage());
                                        bookmark.setCategoryId(categoryId);


                                        if (!localDB.isBookmark(searchAdapter.getRef(position).getKey(),categoryId)) {
                                            localDB.addToBookmark(bookmark);
                                            holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                            Toast.makeText(SearchActivity.this, "已加入書籤", Toast.LENGTH_LONG).show();

                                        } else {
                                            localDB.removeFromBookmark(searchAdapter.getRef(position).getKey(),categoryId);
                                            holder.btn_bookmarks.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                                            Toast.makeText(SearchActivity.this, "已取消書籤", Toast.LENGTH_LONG).show();

                                        }


                                    }
                                });

                                holder.setItemClickListener(new ItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position, boolean isLongClick) {
                                        Intent placeDetail = new Intent(SearchActivity.this, com.travel.gwhk.placeDetail.class);
                                        placeDetail.putExtra("PlaceId", searchAdapter.getRef(position).getKey());//send drinks id to new activity
                                        placeDetail.putExtra("CategoryId", categoryId);
                                        startActivity(placeDetail);
                                    }
                                });

                            }
                        };
                        searchAdapter.startListening();
                        recycler_menu.setAdapter(searchAdapter);
                        if (searchAdapter==null) {
                            Log.e("exist", "exist");
                        }

                    }

                    }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    private void loadSuggest() {
        for (int id = 1; id < 20; id++) {

            final String categoryId = "0" + id;
            place.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Place item = postSnapshot.getValue(Place.class);
                        suggestList.add(item.getName());
                    }

                    materialSearchBar.setLastSuggestions(suggestList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    @Override
    protected void onStop() {
        if (adapter!=null)
            adapter.stopListening();
        if (searchAdapter!=null)
            searchAdapter.stopListening();

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
