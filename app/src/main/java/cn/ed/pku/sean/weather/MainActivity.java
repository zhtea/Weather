package cn.ed.pku.sean.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;

import cn.ed.pku.sean.weather.bean.TodayWeather;
import cn.ed.pku.sean.weather.util.NetUtil;

//import java.util.logging.Handler;
//import java.util.logging.LogRecord;


public class MainActivity extends Activity implements View.OnClickListener{

    private ImageView mUpdateBtn,myCitySelect;
    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv;
    private ImageView weatherImg,pmImg;
    private static final int UPDATE_TODAY_WEATHER = 1;

    /*Button call;*/


    //初始化控件
    void initView(){
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView)findViewById(R.id.humidity);
        weekTv = (TextView)findViewById(R.id.week_today);
        pmDataTv = (TextView)findViewById(R.id.pm_data);
        pmQualityTv = (TextView)findViewById(R.id.pm2_5_quality);
        temperatureTv = (TextView)findViewById(R.id.temperature);
        climateTv = (TextView)findViewById(R.id.climate);
        windTv = (TextView)findViewById(R.id.wind);
        weatherImg = (ImageView)findViewById(R.id.weather_img);
        pmImg = (ImageView)findViewById(R.id.pm2_5_img);
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        windTv.setText("N/A");
        climateTv.setText("N/A");
    }
    String[] save =new String[9];

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        cityTv.setText(savedInstanceState.getString(save[0]));
        timeTv.setText(savedInstanceState.getString(save[1]));
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString(save[0], (String) cityTv.getText());
        outState.putString(save[1],(String)timeTv.getText());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.d(TAG , "MainAcitivity->OnCreate");
        setContentView(R.layout.weather_info);
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        myCitySelect =(ImageView)findViewById(R.id.title_city_manager);
        myCitySelect.setOnClickListener(this);
        /*call =(Button)findViewById(R.id.button3);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i =new Intent(Intent.ACTION_DIAL, Uri.parse("tel://10086"));
                startActivity(i);
            }
        });*/

        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onClick(View view){
        if(view.getId() == R.id.title_city_manager){
            Intent i = new Intent(this,SelectCity.class);
            startActivity(i);
        }
        if(view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);
            if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络ok");
                queryWeatherCode(cityCode);
            }else{
                Log.d("myWeather","网络挂了") ;
                Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
            }

        }
    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather)msg.obj);
                    break;
                default:
                    break;
            }
        }

    };

    //根据城市编号查询对应的天气信息
    private void queryWeatherCode(String cityCode){
        final String adress = "http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        Log.d("myWeather", adress);

        new Thread(new Runnable(){
            @Override
            public void run(){
                TodayWeather todayWeather = null;
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(adress);
                    HttpResponse httpResponse = httpclient.execute(httpget);
                    if(httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        InputStream responseStream = entity.getContent();
                        responseStream = new GZIPInputStream(responseStream);

                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                        StringBuilder response = new StringBuilder();
                        String str;
                        while ((str = reader.readLine()) != null) {
                            response.append(str);
                        }
                        String responseStr = response.toString();
                        Log.d("myWeather", responseStr);
                        //parseXML(responseStr);
                        todayWeather = parseXML(responseStr);
                        if(todayWeather != null){
                           // Log.d("weather",todayWeather.toString());
                            //交由主线程更新UI
                            Message msg =new Message();
                            msg.what = UPDATE_TODAY_WEATHER;
                            msg.obj = todayWeather;
                            mHandler.sendMessage(msg);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //xml 文件解析
    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
        try {
            int fengxiangCount = 0;
            int fengliCount = 0;
            int dateCount = 0;
            int highCount = 0;
            int lowCount = 0;
            int typeCount = 0;
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myapp2","parseXML");
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    //判断是否为文档开始
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断是否为标签开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather = new TodayWeather();
                        }
                        if(todayWeather !=null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                               // Log.d("myapp2", "city: " + xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "updatetime: " + xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "shidu: " + xmlPullParser.getText());
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "wendu: " + xmlPullParser.getText());
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "upm2.5: " + xmlPullParser.getText());
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "quality: " + xmlPullParser.getText());
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "fengxiang: " + xmlPullParser.getText());
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "fengli: " + xmlPullParser.getText());
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "date: " + xmlPullParser.getText());
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "high: " + xmlPullParser.getText());
                                String[] s = xmlPullParser.getText().split(" ");
                                todayWeather.setHigh(s[1]);
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "low: " + xmlPullParser.getText());
                                String[] t =xmlPullParser.getText().split(" ");
                                todayWeather.setLow(t[1]);
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                //Log.d("myapp2", "type: " + xmlPullParser.getText());
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }

                        break;

                    //判断是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    void updateTodayWeather(TodayWeather todayWeather){
        Log.d("weather1", todayWeather.toString());
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力：" + todayWeather.getFengli());
        //System.out.println(todayWeather.getPm25());
         if(Integer.parseInt(todayWeather.getPm25())<=50){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }else if(Integer.parseInt(todayWeather.getPm25())<=100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        }else if(Integer.parseInt(todayWeather.getPm25())<=150){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        }else if(Integer.parseInt(todayWeather.getPm25())<=200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }else if(Integer.parseInt(todayWeather.getPm25())<=300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }else {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }

        if(todayWeather.getType().equals("晴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if(todayWeather.getType().equals("暴雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }else if(todayWeather.getType().equals("暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }else if(todayWeather.getType().equals("大暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }else if(todayWeather.getType().equals("大雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }else if(todayWeather.getType().equals("大雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }else if(todayWeather.getType().equals("多云")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }else if(todayWeather.getType().equals("雷阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }else if(todayWeather.getType().equals("雷阵雨冰雹")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }else if(todayWeather.getType().equals("沙尘暴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }else if(todayWeather.getType().equals("特大暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }else if(todayWeather.getType().equals("雾")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        }else if(todayWeather.getType().equals("小雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }else if(todayWeather.getType().equals("小雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }else if(todayWeather.getType().equals("阴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        }else if(todayWeather.getType().equals("雨夹雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }else if(todayWeather.getType().equals("阵雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }else if(todayWeather.getType().equals("阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }else if(todayWeather.getType().equals("晴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if(todayWeather.getType().equals("中雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }else if(todayWeather.getType().equals("中雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }

        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }
}
