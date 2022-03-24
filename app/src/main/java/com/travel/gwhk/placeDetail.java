package com.travel.gwhk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.travel.gwhk.Common.Common;
import com.travel.gwhk.Database.Database;
import com.travel.gwhk.Model.Bookmark;
import com.travel.gwhk.Model.Place;
import com.travel.gwhk.Model.Place2;
import com.travel.gwhk.Model.Rating;
import com.travel.gwhk.ViewHolder.PlaceViewHolder;

import java.util.HashMap;
import java.util.Map;

public class placeDetail extends AppCompatActivity implements OnMapReadyCallback {
    TextView place_detail, place_name, place_time, place_transport, place_fee;
    TextView rating_score;
    ImageView img_place;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab_bookmarks;
    FloatingActionButton btnBack;
    FloatingActionButton btnRating;

    private AdView bannerAdView;

    String uid;
    RatingBar ratingBar;
    Button login;
    Button view_cm;
    Database localDB;
    FirebaseRecyclerAdapter<Place, PlaceViewHolder> adapter;

    private GoogleMap mMap;
    String str_latitude;
    String str_longitude;

    FirebaseUser user;
    String placeId = "";
    String categoryId = "";
    DatabaseReference place;
    DatabaseReference ratingTbl;

    FirebaseDatabase database;
    Place model;
    Place2 currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        place = database.getReference("Place");
        ratingTbl = database.getReference("Rating");
//        login = (Button) findViewById(R.id.login);

        localDB = new Database(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        place_detail = (TextView) findViewById(R.id.place_detail);
        place_name = (TextView) findViewById(R.id.place_name);
        place_time = (TextView) findViewById(R.id.place_time);
        place_transport = (TextView) findViewById(R.id.place_transportation);
        place_fee = (TextView) findViewById(R.id.place_fee);
        img_place = (ImageView) findViewById(R.id.place_image2);

        fab_bookmarks = (FloatingActionButton) findViewById(R.id.btnbookmarks2);
        btnBack = (FloatingActionButton) findViewById(R.id.btnBack);
        btnRating = (FloatingActionButton) findViewById(R.id.btnrating);
        rating_score=(TextView)findViewById(R.id.rating_score);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        view_cm = (Button)findViewById(R.id.btnComment);

        MobileAds.initialize(this);
        bannerAdView = findViewById(R.id.banner_ad);

        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);
        bannerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //do something on ad is closed
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                //do something if ad failed to load
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });



        view_cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(placeDetail.this,showComment.class);
                    intent.putExtra(Common.INTENT_PLACE_ID,placeId);
                    startActivity(intent);
            }
        });


        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    showLoginDialog();
                } else {
                        showRatingDialog();
                }

            }
        });

        final Place clickItem = model;

        if (getIntent() != null)
            placeId = getIntent().getStringExtra("PlaceId");
            categoryId = getIntent().getStringExtra("CategoryId");


        if (localDB.isBookmark(placeId,categoryId))
            fab_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);


        fab_bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bookmark bookmark = new Bookmark();
                bookmark.setPlaceName(currentPlace.getName());
                bookmark.setPlaceId(placeId);
                bookmark.setPlaceCategory(currentPlace.getCategory());
                bookmark.setPlaceDescription(currentPlace.getDescription());
                bookmark.setPlaceDistrict(currentPlace.getDistrict());
                bookmark.setPlaceRegion(currentPlace.getRegion());
                bookmark.setPlaceImage(currentPlace.getImage());
                bookmark.setCategoryId(categoryId);


                if (!localDB.isBookmark(placeId,categoryId)) {
                    localDB.addToBookmark(bookmark);
                    fab_bookmarks.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    Toast.makeText(placeDetail.this, "已加入書籤", Toast.LENGTH_LONG).show();
                } else {
                    localDB.removeFromBookmark(placeId,categoryId);
                    fab_bookmarks.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    Toast.makeText(placeDetail.this, "已取消書籤", Toast.LENGTH_LONG).show();

                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        if ((!placeId.isEmpty())&& (!categoryId.isEmpty())) {
            getDetailPlace(placeId,categoryId);
            getRatingPlace(placeId,categoryId);
        }
    }


    private void getRatingPlace(String placeId,String categoryId) {
        //Query placeRating = database.getReference().child("Rating").orderByChild("placeId").equalTo(placeId);
        Query placeRating = database.getReference().child("Rating").child(categoryId).child(placeId).orderByChild("placeId").equalTo(placeId);
        placeRating.addValueEventListener(new ValueEventListener() {
            double count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += item.getRateValue();
                    count++;



                }
                if (count != 0) {
                    double average = sum / count;
                    average = Math.round(average * 10.0) / 10.0;
                    rating_score.setText(String.valueOf(average));
                    ratingBar.setRating((float)average);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addRatingtoPlace(final double ratingValue) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Place").child(categoryId).child(placeId);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    Toast.makeText(placeDetail.this,"good",
                            Toast.LENGTH_SHORT);

                    //Place place = dataSnapshot.getValue(Place.class);
                    //place.setKey(Common.selectedPlace.getKey());
                    //ref.child("ratingValue").setValue(ratingValue);
                    //ref.child("ratingValue").setValue(ratingValue);
                    double ratingvalue = dataSnapshot.child("ratingValue").getValue(double.class);
                    double ratingcount = dataSnapshot.child("ratingCount").getValue(double.class);

                    double sumRating = ratingvalue+ratingValue;
                    double sumCount = ratingcount+1;
                    double result = sumRating/sumCount;


                    ref.child("ratingValue").setValue(result);
                    ref.child("ratingCount").setValue(sumCount);

                   /* dataSnapshot.getRef().updateChildren(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful())
                          {

                          }
                        }
                    });*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changeRatingtoPlace(final double oldRating, final double ratingValue) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Place").child(categoryId).child(placeId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Toast.makeText(placeDetail.this, "good",
                            Toast.LENGTH_SHORT);

                    double ratingvalue = dataSnapshot.child("ratingValue").getValue(double.class);
                    double ratingcount = dataSnapshot.child("ratingCount").getValue(double.class);

                    double sumRating = (ratingvalue * ratingcount) - oldRating + ratingValue ;
                    double result = sumRating / ratingcount;

                    Log.d("ratingvalue", String.valueOf(ratingvalue));
                    Log.d("oldRating", String.valueOf(oldRating));
                    Log.d("ratingValue", String.valueOf(ratingValue));

                    ref.child("ratingValue").setValue(result);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(placeDetail.this);
        builder.setTitle("Rating");
        builder.setMessage("分享你對景點的意見");

        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_rating, null);

        final RatingBar ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
        final EditText edtComment = (EditText) itemView.findViewById(R.id.edt_comment);

        builder.setView(itemView);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uid = user.getUid();

                final Rating rating = new Rating();
                rating.setUid(user.getUid());
                rating.setComment(edtComment.getText().toString());
                rating.setPlaceId(placeId);
                rating.setRateValue(ratingBar.getRating());
                Map<String,Object> serverTimeStamp = new HashMap<>();
                serverTimeStamp.put("timeStamp", ServerValue.TIMESTAMP);
                rating.setCommentTimeStamp(serverTimeStamp);

                Query placeRating = database.getReference().child("Rating").child(categoryId).child(placeId).orderByChild("uid").equalTo(uid);
                placeRating.addListenerForSingleValueEvent(new ValueEventListener() {
                    double oldRating;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                Rating item  = childSnapshot.getValue(Rating.class);
                                oldRating = item.getRateValue();

                                String uidx = item.getUid();
                                String placeid = item.getPlaceId();

                                childSnapshot.getRef().removeValue();
                                Log.d("on9", "bad");

                            }



                            ratingTbl.child(categoryId).child(placeId).push().setValue(rating);

                            changeRatingtoPlace(oldRating,ratingBar.getRating());

                        }

                        else
                        {
                            ratingTbl.child(categoryId).child(placeId).push().setValue(rating);
                            addRatingtoPlace(ratingBar.getRating());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                    });

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void getDetailPlace(String placeId,String categoryId) {
        place.child(categoryId).child(placeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPlace = dataSnapshot.getValue(Place2.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentPlace.getImage()).into(img_place);

                collapsingToolbarLayout.setTitle(currentPlace.getName());

                place_name.setText(currentPlace.getName());

                place_detail.setText(currentPlace.getDetail());

                place_transport.setText(currentPlace.getTransport());

                place_time.setText(currentPlace.getTime());

                place_fee.setText(currentPlace.getFee());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        place.child(categoryId).child(placeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPlace = dataSnapshot.getValue(Place2.class);
                String s_latitude = currentPlace.getLatitude();
                String s_longitude = currentPlace.getLongitude();
                // Add a marker in Sydney and move the camera
                double latitude = Double.parseDouble(s_latitude);
                double longitude = Double.parseDouble(s_longitude);

                LatLng places = new LatLng(latitude, longitude);

                mMap.addMarker(new MarkerOptions().position(places).title("").snippet(""));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(places, 14));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(placeDetail.this);
        dialog.setTitle("撰寫評論");
        dialog.setMessage("請先登入");

        dialog.setPositiveButton("登入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent2 = new Intent(placeDetail.this, LoginActivity.class);
                startActivity(intent2);
            }
        });
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }
}




