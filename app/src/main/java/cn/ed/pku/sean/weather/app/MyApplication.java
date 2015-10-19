package cn.ed.pku.sean.weather.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.ed.pku.sean.weather.bean.City;
import cn.ed.pku.sean.weather.db.CityDB;

/**
 * Created by Bryce on 2015/10/18.
 */
public class MyApplication extends Application{
    protected static final String TAG= "MyAPP";
    protected static Application mApplication;
    List<City> mCityList;
    CityDB mCityDB;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"MyApplication->OnCreate");
        mApplication = this;

        mCityDB =openCityDB();
        initCityList();
    }
    public static Application getInstance(){
        return mApplication;
    }

    //初始化数据库
    private CityDB openCityDB(){
        String path = "/data"+ Environment.getDataDirectory().getAbsolutePath()+ File.separator+
                getPackageName()+File.separator+"databases"+File.separator+CityDB.CITY_DB_NAME;
        File db =new File(path);
        Log.d("MyApp",path);
        if(!db.exists()){
            Log.i("MyApp","db is not exists");
            try {
                InputStream is =getAssets().open("city.db");
                FileOutputStream fos =new FileOutputStream(db);
                int len =-1;
                byte[] buffer = new byte[1024];
                while((len =is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }

    //初始化城市信息列表

    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        for(City city :mCityList){
            String cityName = city.getCity();
            Log.d("MyApp",cityName);
        }
        return true;
    }
}
