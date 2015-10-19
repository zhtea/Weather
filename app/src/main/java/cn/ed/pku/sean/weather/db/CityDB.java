package cn.ed.pku.sean.weather.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.ed.pku.sean.weather.bean.City;

/**
 * Created by Bryce on 2015/10/18.
 */
 public class CityDB {
    public static final String CITY_DB_NAME ="city2.db";
    private static final String CITY_TABLE_NAME ="city";
    private SQLiteDatabase db;


    public CityDB(Context context,String path){
        db = context.openOrCreateDatabase(CITY_DB_NAME,Context.MODE_PRIVATE,null);
    }
    public CityDB(){

    }

    public List<City> getAllCity(){
        List<City> list =new ArrayList<City>();
        Cursor c=db.rawQuery("SELECT * from"+CITY_TABLE_NAME,null);
        while(c.moveToNext()){
            String province =c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allPY"));
            String allFirstPY=c.getString(c.getColumnIndex("allFirstPY"));
            String firstPY =c.getString(c.getColumnIndex("firstPY"));
            City item =new City(province,city,number,firstPY,allPY,allFirstPY);
            list.add(item);
        }
        return list;
    }



}
