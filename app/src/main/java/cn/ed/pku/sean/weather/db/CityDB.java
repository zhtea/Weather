package cn.ed.pku.sean.weather.db;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.ed.pku.sean.weather.SelectCity;
import cn.ed.pku.sean.weather.bean.City;

/**
 * Created by Bryce on 2015/10/18.
 */
 public class CityDB extends Activity{
    public static final String CITY_DB_NAME ="city2.db";
    private static final String CITY_TABLE_NAME ="city";
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=this.getIntent();
        String search=intent.getStringExtra("search");
        ArrayList<String> s=searchCity(search);
        Intent back =new Intent(this, SelectCity.class);
        back.putExtra("res",s);
    }

    public  CityDB(Context context,String path){
        db = context.openOrCreateDatabase(CITY_DB_NAME,Context.MODE_PRIVATE,null);
    }
    public CityDB(){}

    public List<City> getAllCity(){
        List<City> list =new ArrayList<City>();
        Cursor c=db.rawQuery("SELECT * from "+CITY_TABLE_NAME,null);
        while(c.moveToNext()){
            String province =c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY=c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY =c.getString(c.getColumnIndex("firstpy"));
            City item =new City(province,city,number,firstPY,allPY,allFirstPY);
            list.add(item);
        }
        return list;
    }
    public ArrayList<String> searchCity(String s){
        ArrayList<String> str = new ArrayList<String>();
       Cursor cur = db.rawQuery("SELECT * from "+CITY_TABLE_NAME+" where city"+"=\""+s+"\"",null);
        while(cur.moveToNext())
            str.add(cur.getString(cur.getColumnIndex("city")));
        return str;
    }
  public String getCityCode(String s){

     Cursor cur = db.rawQuery("SELECT number from " + CITY_TABLE_NAME + " where city" + "=\"" + s + "\"", null);
      if(cur.moveToFirst())
 return cur.getString(cur.getColumnIndex("number"));
      return "12345";

  }



}
