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
    private static final String TAG= "MyAPP";
    private static Application mApplication;
   public static List<City> mCityList;
    public static CityDB mCityDB;
   // public  String[] str=new String[mCityList.size()];


    @Override
    public void  onCreate(){
        super.onCreate();
        Log.d(TAG,"MyApplication->OnCreate");
        mApplication = this;

        mCityDB =openCityDB();
        initCityList();
    }
    public  Application getInstance(){
        return mApplication;
    }

    //初始化数据库
    private  CityDB  openCityDB(){
        String path = "/data"+ Environment.getDataDirectory().getAbsolutePath()+ File.separator+
                getPackageName()+File.separator+"databases"+File.separator;
       // File db =new File(path);
        File dir =new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File db = new File(path+CityDB.CITY_DB_NAME);
        Log.d(TAG,path);
        if(!db.exists()){
            Log.i("MyApp","db is not exists");
            try {
                InputStream is =getAssets().open("city.db");
                FileOutputStream fos =new FileOutputStream(db);
                int len = -1;
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

    private void  initCityList(){
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

      //  int i=0;
      //  Intent intent = new Intent(MyApplication.this, SelectCity.class);
       /* for(City city :mCityList){
            String cityName = city.getCity();
     //       str[i]=cityName;
           // Log.d("MyApp",cityName);
        }
      //  intent.putExtra("st", str);
      //  startActivity(intent);*/
        return true;
    }

}
