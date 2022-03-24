package com.travel.gwhk.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.travel.gwhk.Model.Bookmark;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper{
    private static final String DB_Name="gwhkDB3.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super (context, DB_Name, null, DB_VER);
    }


    public List<Bookmark> getAllPlaces ()
    {
        SQLiteDatabase db= getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"PlaceID, PlaceName, PlaceDescription, PlaceCategory, PlaceDistrict,PlaceRegion,PlaceImage,CategoryId"};
        String sqlTable = "Bookmark";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null,null,null);

        final List<Bookmark> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do {
                result.add(new Bookmark(
                        c.getString(c.getColumnIndex("PlaceID")),
                        c.getString(c.getColumnIndex("PlaceName")),
                        c.getString(c.getColumnIndex("PlaceDescription")),
                        c.getString(c.getColumnIndex("PlaceCategory")),
                        c.getString(c.getColumnIndex("PlaceDistrict")),
                        c.getString(c.getColumnIndex("PlaceRegion")),
                        c.getString(c.getColumnIndex("PlaceImage")),
                        c.getString(c.getColumnIndex("CategoryId"))
                ));
            } while(c.moveToNext());
        }
        return result;
    }

    //Bookmarks
    public void addToBookmark(Bookmark Place){

        SQLiteDatabase db=getReadableDatabase();
        String query = String.format("INSERT INTO Bookmark(PlaceID, PlaceName, PlaceDescription, PlaceCategory, PlaceDistrict,PlaceRegion,PlaceImage,CategoryId) VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                Place.getPlaceId(),
                Place.getPlaceName(),
                Place.getPlaceDescription(),
                Place.getPlaceCategory(),
                Place.getPlaceDistrict(),
                Place.getPlaceRegion(),
                Place.getPlaceImage(),
                Place.getCategoryId()
                );
        db.execSQL(query);
    }

    public void removeFromBookmark(String PlaceId,String CategoryId){

        SQLiteDatabase db=getReadableDatabase();
        String query = String.format("DELETE FROM Bookmark WHERE (PlaceID='%s'AND CategoryId='%s');",PlaceId, CategoryId);;
        db.execSQL(query);
    }

    public boolean isBookmark (String PlaceId,String CategoryId){

        SQLiteDatabase db=getReadableDatabase();
        String query = String.format("SELECT * FROM Bookmark WHERE (PlaceID='%s' AND CategoryId='%s');",PlaceId, CategoryId);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <= 0){

            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }


}
