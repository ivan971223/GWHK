package com.travel.gwhk.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.travel.gwhk.HttpRequest;
import com.travel.gwhk.Interface.ItemClickListener;
import com.travel.gwhk.Model.Category;
import com.travel.gwhk.R;
import com.travel.gwhk.SearchActivity;
import com.travel.gwhk.ViewHolder.CardViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Context mContext;

    EditText mSearchField;
    ImageButton mSearchBtn;
    ImageButton btnBookmarks;

    DatabaseReference category;
    FirebaseDatabase database;

    RecyclerView recycler_menu;

    FirebaseRecyclerAdapter<Category, CardViewHolder> adapter;

    ProgressBar loader2;

    TextView ErrorTxt2;

    RelativeLayout mainContainer;

    String API = "bc1193ea68c60f55d990377d3394a225";

    TextView Curr_addressTxt, Curr_dateTxt,Curr_updated_atTxt, Curr_statusTxt, Curr_tempTxt, Curr_sunriseTxt,
            Curr_sunsetTxt, Curr_humidityTxt, Curr_UVTxt;
    TextView updated_atTxt2,updated_atTxt3,updated_atTxt4,updated_atTxt5,updated_atTxt6,updated_atTxt7;
    TextView tempTxt2,tempTxt3,tempTxt4,tempTxt5,tempTxt6,tempTxt7;
    TextView statusTxt2, statusTxt3,statusTxt4,statusTxt5,statusTxt6,statusTxt7;
    ImageView Curr_icon,iconView2,iconView3,iconView4,iconView5,iconView6,iconView7;

    String LAT ="22.29";
    String LON ="114.16";

    private InterstitialAd interstitialAd;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_homepage, container, false);


        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        mSearchField = (EditText) view.findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) view.findViewById(R.id.search_btn);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                String input = mSearchField.getText().toString();
                intent.putExtra("searchText",  input );
                startActivity(intent);
            }
        });

        mainContainer=view.findViewById(R.id.mainContainer2);

        Curr_dateTxt = view.findViewById(R.id.curr_date);
        Curr_updated_atTxt = view.findViewById(R.id.updatedAtText);
        Curr_statusTxt = view.findViewById(R.id.curr_status);
        Curr_tempTxt = view.findViewById(R.id.curr_temp);
        Curr_sunriseTxt = view.findViewById(R.id.curr_sunrise);
        Curr_sunsetTxt = view.findViewById(R.id.curr_sunset);
        Curr_humidityTxt = view.findViewById(R.id.curr_humidity);
        Curr_UVTxt = view.findViewById(R.id.curr_uvi);
        Curr_icon = view.findViewById(R.id.curr_icon);

        tempTxt2 = view.findViewById(R.id.temp2);
        updated_atTxt2 =view.findViewById(R.id.date2);
        statusTxt2 = view.findViewById(R.id.status2);
        iconView2 = view.findViewById(R.id.icon2);

        tempTxt3 = view.findViewById(R.id.temp3);
        updated_atTxt3 = view.findViewById(R.id.date3);
        statusTxt3 = view.findViewById(R.id.status3);
        iconView3 = view.findViewById(R.id.icon3);

        tempTxt4 = view.findViewById(R.id.temp4);
        updated_atTxt4 = view.findViewById(R.id.date4);
        statusTxt4 = view.findViewById(R.id.status4);
        iconView4 = view.findViewById(R.id.icon4);

        tempTxt5 = view.findViewById(R.id.temp5);
        updated_atTxt5 = view.findViewById(R.id.date5);
        statusTxt5 = view.findViewById(R.id.status5);
        iconView5 = view.findViewById(R.id.icon5);

        tempTxt6 = view.findViewById(R.id.temp6);
        updated_atTxt6 = view.findViewById(R.id.date6);
        statusTxt6 = view.findViewById(R.id.status6);
        iconView6 = view.findViewById(R.id.icon6);

        tempTxt7 = view.findViewById(R.id.temp7);
        updated_atTxt7 = view.findViewById(R.id.date7);
        statusTxt7 = view.findViewById(R.id.status7);
        iconView7 = view.findViewById(R.id.icon7);

        loader2 = view.findViewById(R.id.loader2);

        ErrorTxt2 = view.findViewById(R.id.errorText2);



        new weatherTask().execute();

        recycler_menu = view.findViewById(R.id.recycler_category);

        recycler_menu.setHasFixedSize(true);

       recycler_menu.setLayoutManager(new GridLayoutManager(getActivity(),2));


       loadCategory();


        return view;
    }

    private void loadCategory() {

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>().setQuery(category, Category.class).setLifecycleOwner(getActivity()).build();

        adapter = new FirebaseRecyclerAdapter<Category, CardViewHolder>(options) {

            @NonNull
            @Override
            public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
                return new CardViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CardViewHolder holder, int position, @NonNull Category model) {
                holder.txtCatergoryName.setText(model.getName());
                Picasso.with(getActivity().getBaseContext()).load(model.getImage()).into(holder.imageCatView);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent placeList = new Intent(getActivity(), com.travel.gwhk.placeList.class);
                        placeList.putExtra("CategoryId", adapter.getRef(position).getKey());//send drinks id to new activity
                        startActivity(placeList);
                    }
                });

            }
        };

        adapter.notifyDataSetChanged();


        recycler_menu.setAdapter(adapter);
    }

    private void firebasePlaceSearch(String searchText) {
        Toast.makeText(getActivity(), "Searching", Toast.LENGTH_LONG).show();

        //Query firebaseSearchQuery = category.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");

       // FirebaseRecyclerOptions<Place> options =
               // new FirebaseRecyclerOptions.Builder<Place>().setQuery(firebaseSearchQuery , Place.class).setLifecycleOwner(this).build();

        //adapter.updateOptions(options);

    }


    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            loader2.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
            ErrorTxt2.setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/onecall?lat="+LAT+"&lon="+LON+"&%20exclude=hourly&lang=zh_tw&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);

                JSONObject today = jsonObj.getJSONObject("current");
                JSONObject weather = today.getJSONArray("weather").getJSONObject(0);

                JSONObject secondday = jsonObj.getJSONArray("daily").getJSONObject(1);
                JSONObject thirdday = jsonObj.getJSONArray("daily").getJSONObject(2);
                JSONObject fourthday = jsonObj.getJSONArray("daily").getJSONObject(3);
                JSONObject fifthday = jsonObj.getJSONArray("daily").getJSONObject(4);
                JSONObject sixthday = jsonObj.getJSONArray("daily").getJSONObject(5);
                JSONObject seventhday = jsonObj.getJSONArray("daily").getJSONObject(6);

                JSONObject secondday_temp = secondday.getJSONObject("temp");
                JSONObject thirdday_temp = thirdday.getJSONObject("temp");
                JSONObject fourthday_temp = fourthday.getJSONObject("temp");
                JSONObject fifthday_temp = fifthday.getJSONObject("temp");
                JSONObject sixthday_temp = sixthday.getJSONObject("temp");
                JSONObject seventhday_temp = seventhday.getJSONObject("temp");


                //Current
                Long updatedAt = today.getLong("dt");
                String updatedAtText = "更新於: " + new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                Long tdydate = today.getLong("dt");
                String tdy_date = new SimpleDateFormat("MMM d, EEE", Locale.TRADITIONAL_CHINESE).format(new Date(tdydate * 1000));
                String tdy_temp = today.getInt("temp") + "°C";
                String tdy_humid = today.getString("humidity")+"%";
                Long sunrise = today.getLong("sunrise");
                Long sunset = today.getLong("sunset");
                String uvi = today.getString("uvi");
                String weatherDescription = weather.getString("description");
                String weatherIcon = weather.getString("icon");


                //other days
                Long updatedAt2 = secondday.getLong("dt");
                Long updatedAt3 = thirdday.getLong("dt");
                Long updatedAt4 = fourthday.getLong("dt");
                Long updatedAt5 = fifthday.getLong("dt");
                Long updatedAt6 = sixthday.getLong("dt");
                Long updatedAt7 = seventhday.getLong("dt");

                String updatedAtText2 = new SimpleDateFormat("MMM d, EEE", Locale.TRADITIONAL_CHINESE).format(new Date(updatedAt2 * 1000));
                String updatedAtText3 =  new SimpleDateFormat("MMM d, EEE", Locale.TRADITIONAL_CHINESE).format(new Date(updatedAt3* 1000));
                String updatedAtText4 = new SimpleDateFormat("MMM d, EEE", Locale.TRADITIONAL_CHINESE).format(new Date(updatedAt4 * 1000));
                String updatedAtText5 = new SimpleDateFormat("MMM d, EEE", Locale.TRADITIONAL_CHINESE).format(new Date(updatedAt5 * 1000));
                String updatedAtText6 = new SimpleDateFormat("MMM d, EEE", Locale.TRADITIONAL_CHINESE).format(new Date(updatedAt6 * 1000));
                String updatedAtText7 = new SimpleDateFormat("MMM d, EEE", Locale.TRADITIONAL_CHINESE).format(new Date(updatedAt7 * 1000));


                String temp2 = secondday_temp.getInt("day") + "°C";
                String temp3 = thirdday_temp.getInt("day") + "°C";
                String temp4 = fourthday_temp.getInt("day") + "°C";
                String temp5 = fifthday_temp.getInt("day") + "°C";
                String temp6 = sixthday_temp.getInt("day") + "°C";
                String temp7 = seventhday_temp.getInt("day") + "°C";

                JSONObject weather2 = secondday.getJSONArray("weather").getJSONObject(0);
                JSONObject weather3 = thirdday.getJSONArray("weather").getJSONObject(0);
                JSONObject weather4 = fourthday.getJSONArray("weather").getJSONObject(0);
                JSONObject weather5 = fifthday.getJSONArray("weather").getJSONObject(0);
                JSONObject weather6 = sixthday.getJSONArray("weather").getJSONObject(0);
                JSONObject weather7 = seventhday.getJSONArray("weather").getJSONObject(0);


                String weatherDescription2 = weather2.getString("description");
                String weatherDescription3 = weather3.getString("description");
                String weatherDescription4 = weather4.getString("description");
                String weatherDescription5 = weather5.getString("description");
                String weatherDescription6 = weather6.getString("description");
                String weatherDescription7 = weather7.getString("description");

                String weatherIcon2 = weather2.getString("icon");
                String weatherIcon3 = weather3.getString("icon");
                String weatherIcon4 = weather4.getString("icon");
                String weatherIcon5 = weather5.getString("icon");
                String weatherIcon6 = weather6.getString("icon");
                String weatherIcon7 = weather7.getString("icon");



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                //Curr_addressTxt.setText(address);

                Curr_updated_atTxt.setText(updatedAtText);
                Curr_dateTxt.setText(tdy_date);
                Curr_statusTxt.setText(weatherDescription);
                Curr_tempTxt.setText(tdy_temp);
                Curr_sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                Curr_sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                Curr_humidityTxt.setText(tdy_humid);
                Curr_UVTxt.setText(uvi);

                switch (weatherIcon){
                    case "01d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d01));
                        break;
                    case "02d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d02));
                        break;
                    case "03d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d03));
                        break;
                    case "04d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d04));
                        break;
                    case "09d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d09));
                        break;
                    case "10d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d10));
                        break;
                    case "11d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d11));
                        break;
                    case "13d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d13));
                        break;
                    case "50d":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d50));
                        break;
                    case "01n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n01));
                        break;

                    case "02n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n02));
                        break;

                    case "03n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n03));
                        break;

                    case "04n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n04));
                        break;

                    case "09n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n09));
                        break;

                    case "10n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n10));
                        break;

                    case "11n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n11));
                        break;

                    case "13n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n13));
                        break;

                    case "50n":
                        Curr_icon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n50));
                        break;
                }
                switch (weatherIcon2){
                    case "01d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d01));
                        break;
                    case "02d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d02));
                        break;
                    case "03d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d03));
                        break;
                    case "04d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d04));
                        break;
                    case "09d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d09));
                        break;
                    case "10d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d10));
                        break;
                    case "11d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d11));
                        break;
                    case "13d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d13));
                        break;
                    case "50d":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d50));
                        break;
                    case "01n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n01));
                        break;

                    case "02n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n02));
                        break;

                    case "03n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n03));
                        break;

                    case "04n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n04));
                        break;

                    case "09n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n09));
                        break;

                    case "10n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n10));
                        break;

                    case "11n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n11));
                        break;

                    case "13n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n13));
                        break;

                    case "50n":
                        iconView2.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n50));
                        break;
                }
                switch (weatherIcon3){
                    case "01d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d01));
                        break;
                    case "02d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d02));
                        break;
                    case "03d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d03));
                        break;
                    case "04d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d04));
                        break;
                    case "09d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d09));
                        break;
                    case "10d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d10));
                        break;
                    case "11d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d11));
                        break;
                    case "13d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d13));
                        break;
                    case "50d":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d50));
                        break;
                    case "01n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n01));
                        break;

                    case "02n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n02));
                        break;

                    case "03n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n03));
                        break;

                    case "04n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n04));
                        break;

                    case "09n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n09));
                        break;

                    case "10n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n10));
                        break;

                    case "11n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n11));
                        break;

                    case "13n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n13));
                        break;

                    case "50n":
                        iconView3.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n50));
                        break;
                }
                switch (weatherIcon4){
                    case "01d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d01));
                        break;
                    case "02d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d02));
                        break;
                    case "03d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d03));
                        break;
                    case "04d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d04));
                        break;
                    case "09d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d09));
                        break;
                    case "10d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d10));
                        break;
                    case "11d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d11));
                        break;
                    case "13d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d13));
                        break;
                    case "50d":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d50));
                        break;
                    case "01n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n01));
                        break;

                    case "02n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n02));
                        break;

                    case "03n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n03));
                        break;

                    case "04n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n04));
                        break;

                    case "09n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n09));
                        break;

                    case "10n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n10));
                        break;

                    case "11n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n11));
                        break;

                    case "13n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n13));
                        break;

                    case "50n":
                        iconView4.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n50));
                        break;
                }
                switch (weatherIcon5){
                    case "01d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d01));
                        break;
                    case "02d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d02));
                        break;
                    case "03d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d03));
                        break;
                    case "04d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d04));
                        break;
                    case "09d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d09));
                        break;
                    case "10d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d10));
                        break;
                    case "11d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d11));
                        break;
                    case "13d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d13));
                        break;
                    case "50d":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d50));
                        break;
                    case "01n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n01));
                        break;

                    case "02n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n02));
                        break;

                    case "03n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n03));
                        break;

                    case "04n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n04));
                        break;

                    case "09n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n09));
                        break;

                    case "10n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n10));
                        break;

                    case "11n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n11));
                        break;

                    case "13n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n13));
                        break;

                    case "50n":
                        iconView5.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n50));
                        break;
                }
                switch (weatherIcon6){
                    case "01d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d01));
                        break;
                    case "02d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d02));
                        break;
                    case "03d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d03));
                        break;
                    case "04d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d04));
                        break;
                    case "09d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d09));
                        break;
                    case "10d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d10));
                        break;
                    case "11d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d11));
                        break;
                    case "13d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d13));
                        break;
                    case "50d":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d50));
                        break;
                    case "01n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n01));
                        break;

                    case "02n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n02));
                        break;

                    case "03n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n03));
                        break;

                    case "04n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n04));
                        break;

                    case "09n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n09));
                        break;

                    case "10n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n10));
                        break;

                    case "11n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n11));
                        break;

                    case "13n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n13));
                        break;

                    case "50n":
                        iconView6.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n50));
                        break;
                }
                switch (weatherIcon7){
                    case "01d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d01));
                        break;
                    case "02d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d02));
                        break;
                    case "03d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d03));
                        break;
                    case "04d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d04));
                        break;
                    case "09d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d09));
                        break;
                    case "10d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d10));
                        break;
                    case "11d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d11));
                        break;
                    case "13d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d13));
                        break;
                    case "50d":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.d50));
                        break;
                    case "01n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n01));
                        break;

                    case "02n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n02));
                        break;

                    case "03n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n03));
                        break;

                    case "04n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n04));
                        break;

                    case "09n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n09));
                        break;

                    case "10n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n10));
                        break;

                    case "11n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n11));
                        break;

                    case "13n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n13));
                        break;

                    case "50n":
                        iconView7.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.n50));
                        break;
                }


                updated_atTxt2.setText(updatedAtText2);
                updated_atTxt3.setText(updatedAtText3);
                updated_atTxt4.setText(updatedAtText4);
                updated_atTxt5.setText(updatedAtText5);
                updated_atTxt6.setText(updatedAtText6);
                updated_atTxt7.setText(updatedAtText7);

                tempTxt2.setText(temp2);
                tempTxt3.setText(temp3);
                tempTxt4.setText(temp4);
                tempTxt5.setText(temp5);
                tempTxt6.setText(temp6);
                tempTxt7.setText(temp7);

                statusTxt2.setText(weatherDescription2);
                statusTxt3.setText(weatherDescription3);
                statusTxt4.setText(weatherDescription4);
                statusTxt5.setText(weatherDescription5);
                statusTxt6.setText(weatherDescription6);
                statusTxt7.setText(weatherDescription7);



                /* Views populated, Hiding the loader, Showing the main design */
                loader2.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);


            } catch (JSONException e) {
               loader2.setVisibility(View.GONE);
                ErrorTxt2.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}



