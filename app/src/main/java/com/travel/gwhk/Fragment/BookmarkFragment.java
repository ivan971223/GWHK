package com.travel.gwhk.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.travel.gwhk.Database.Database;
import com.travel.gwhk.Model.Bookmark;
import com.travel.gwhk.R;
import com.travel.gwhk.adapter.BookmarkAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Database localDB;
    BookmarkAdapter adapter;

    List<Bookmark> bookmarkList = new ArrayList<>();
    String placeId="";

    ImageButton btnBookmark2;
    RecyclerView.ViewHolder BookmarkViewHolder;

    DatabaseReference category;
    FirebaseDatabase database;

    RecyclerView recycler_menu;
    private AdView bannerAdView;


    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的書籤");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView = (RecyclerView)view.findViewById(R.id.bookmark_list);

        btnBookmark2 =(ImageButton) view.findViewById(R.id.btn_bookmark2);

        localDB = new Database(getActivity());
        MobileAds.initialize(getActivity());
        bannerAdView = view.findViewById(R.id.banner_ad);

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


        loadListPlace();



        return view;
    }



    private void loadListPlace() {

        adapter = new BookmarkAdapter(new Database(getActivity()).getAllPlaces(),getActivity());

        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

    }




}
