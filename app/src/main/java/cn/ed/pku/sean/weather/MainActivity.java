package cn.ed.pku.sean.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import cn.ed.pku.sean.weather.bean.FutureWeather;
import cn.ed.pku.sean.weather.bean.TodayWeather;
import cn.ed.pku.sean.weather.util.NetUtil;
import cn.ed.pku.sean.weather.util.WeatherAdapter;

//import java.util.logging.Handler;
//import java.util.logging.LogRecord;


public class MainActivity extends Activity implements View.OnClickListener{

    private ImageView mUpdateBtn,myCitySelect,mTitleShare,mTitleLocation;
    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv;
    private ImageView weatherImg,pmImg;
    private ProgressBar mUpdateProgressbar;
    private static final int UPDATE_TODAY_WEATHER = 1;
    private WeatherAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    private TextView[] FurWeeks = new TextView[6];
    private TextView[] FurTemps = new TextView[6];
    private TextView[] FurClis = new TextView[6];
    private TextView[] FurWinds = new TextView[6];
    private ImageView[] FurImages = new ImageView[6];

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
        LayoutInflater inflater = LayoutInflater.from(this);
        View one_page = inflater.inflate(R.layout.frag1, null);
        View two_page = inflater.inflate(R.layout.frag2, null);
        views = new ArrayList<View>();
        views.add(one_page);
        views.add(two_page);
        vpAdapter = new WeatherAdapter(views, this);
        vp = (ViewPager)findViewById(R.id.viewpager1);
        vp.setAdapter(vpAdapter);


        FurWeeks[0] = (TextView)one_page.findViewById(R.id.weekDay1);
        FurTemps[0] = (TextView)one_page.findViewById(R.id.temperatureDay1);
        FurClis[0] = (TextView)one_page.findViewById(R.id.climateDay1);
        FurWinds[0] = (TextView)one_page.findViewById(R.id.windDay1);
        FurImages[0] = (ImageView)one_page.findViewById(R.id.imageDay1);

        FurWeeks[1] = (TextView)one_page.findViewById(R.id.weekDay2);
        FurTemps[1] = (TextView)one_page.findViewById(R.id.temperatureDay2);
        FurClis[1] = (TextView)one_page.findViewById(R.id.climateDay2);
        FurWinds[1] = (TextView)one_page.findViewById(R.id.windDay2);
        FurImages[1] = (ImageView)one_page.findViewById(R.id.imageDay2);

        FurWeeks[2] = (TextView)one_page.findViewById(R.id.weekDay3);
        FurTemps[2] = (TextView)one_page.findViewById(R.id.temperatureDay3);
        FurClis[2] = (TextView)one_page.findViewById(R.id.climateDay3);
        FurWinds[2] = (TextView)one_page.findViewById(R.id.windDay3);
        FurImages[2] = (ImageView)one_page.findViewById(R.id.imageDay3);

        FurWeeks[3] = (TextView)two_page.findViewById(R.id.weekDay4);
        FurTemps[3] = (TextView)two_page.findViewById(R.id.temperatureDay4);
        FurClis[3] = (TextView)two_page.findViewById(R.id.climateDay4);
        FurWinds[3] = (TextView)two_page.findViewById(R.id.windDay4);
        FurImages[3] = (ImageView)two_page.findViewById(R.id.imageDay4);

        FurWeeks[4] = (TextView)two_page.findViewById(R.id.weekDay5);
        FurTemps[4] = (TextView)two_page.findViewById(R.id.temperatureDay5);
        FurClis[4] = (TextView)two_page.findViewById(R.id.climateDay5);
        FurWinds[4] = (TextView)two_page.findViewById(R.id.windDay5);
        FurImages[4] = (ImageView)two_page.findViewById(R.id.imageDay5);

        FurWeeks[5] = (TextView)two_page.findViewById(R.id.weekDay6);
        FurTemps[5] = (TextView)two_page.findViewById(R.id.temperatureDay6);
        FurClis[5] = (TextView)two_page.findViewById(R.id.climateDay6);
        FurWinds[5] = (TextView)two_page.findViewById(R.id.windDay6);
        FurImages[5] = (ImageView)two_page.findViewById(R.id.imageDay6);


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
        outState.putString(save[1], (String) timeTv.getText());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Log.d(TAG , "MainAcitivity->OnCreate");
        setContentView(R.layout.weather_info);
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateProgressbar = (ProgressBar)findViewById(R.id.title_progress_bar);
        mTitleLocation=(ImageView)findViewById(R.id.title_location);
        mTitleShare=(ImageView)findViewById(R.id.title_share);
        mUpdateProgressbar.setVisibility(View.GONE);
        mUpdateBtn.setOnClickListener(this);
        myCitySelect =(ImageView)findViewById(R.id.title_city_manager);
        myCitySelect.setOnClickListener(this);
        updateCity();
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
            mUpdateBtn.setVisibility(View.INVISIBLE);
            mTitleLocation.setVisibility(View.INVISIBLE);
            mTitleShare.setVisibility(View.INVISIBLE);
            mUpdateProgressbar.setVisibility(View.VISIBLE);
            updateCity();


            /*mUpdateProgressbar.setVisibility(View.GONE);
            mUpdateBtn.setVisibility(View.VISIBLE);
            mTitleLocation.setVisibility(View.VISIBLE);
            mTitleShare.setVisibility(View.VISIBLE);*/

        }
    }

    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    TodayWeather todayWeather = (TodayWeather)msg.getData().get("todayWeather");
                    List<FutureWeather>list = (List<FutureWeather>)msg.obj;
                    updateTodayWeather(todayWeather,list);
                    mUpdateProgressbar.setVisibility(View.GONE);
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    mTitleLocation.setVisibility(View.VISIBLE);
                    mTitleShare.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

    };

    //刷新
    private void updateCity(){
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String s ="101010100";
        Intent intent =getIntent();
        if(intent.getExtras()!=null)
            s= intent.getStringExtra("city");

        String cityCode = s;
        Log.e("123", cityCode);
        Log.e("345", s);
        if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
            Log.d("myWeather", "网络ok");
            // final ProgressDialog proDialog = android.app.ProgressDialog.show(MainActivity.this, "正在刷新", "2秒后自动消失！");
            queryWeatherCode(cityCode);
                /*Thread thread = new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            sleep(2000);
                        } catch (InterruptedException e)
                        {
                            // TODO 自动生成的 catch 块
                            e.printStackTrace();
                        }
                        proDialog.dismiss();//万万不可少这句，否则会程序会卡死。
                    }
                };
                thread.start();*/
        }else{
            Log.d("myWeather", "网络挂了") ;
            Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
        }
    }

    //根据城市编号查询对应的天气信息
    private void queryWeatherCode(String cityCode){
        final String adress = "http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        Log.d("myWeather", adress);

        new Thread(new Runnable(){
            @Override
            public void run(){
                TodayWeather todayWeather1 = null;
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
                        todayWeather1 = parseXML(responseStr);
                        List<FutureWeather> lists = pullParseXML(responseStr);

                        if(todayWeather1 != null&&lists!=null){
                           // Log.d("weather",todayWeather.toString());
                            //交由主线程更新UI
                            Message msg =new Message();
                            msg.what = UPDATE_TODAY_WEATHER;
                            msg.obj = lists;
                            Bundle data = new Bundle();
                            data.putSerializable("todayWeather",todayWeather1);
                            msg.setData(data);
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
            int type_1Count=0;
            int fx_1Count=0;
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
                            }else if (xmlPullParser.getName().equals("date_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFdate0(xmlPullParser.getText());
                            }  else if (xmlPullParser.getName().equals("high_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFhigh0(xmlPullParser.getText().substring(2).trim());
                            } else if (xmlPullParser.getName().equals("low_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFlow0(xmlPullParser.getText().substring(2).trim());
                            }else if (xmlPullParser.getName().equals("type_1") && type_1Count==0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFtype0(xmlPullParser.getText());
                                type_1Count++;
                            } else if (xmlPullParser.getName().equals("fx_1") && fx_1Count==0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFfengxiang0(xmlPullParser.getText());
                                fx_1Count++;
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

    private List<FutureWeather> pullParseXML(String xmldata) {
        List<FutureWeather> lists = null;
        FutureWeather futureWeather = null;
        try{
            int typeCou = 0;
            int fengxiangCou = 0;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser pullParser = factory.newPullParser();
            pullParser.setInput(new StringReader(xmldata));
            int eventType = pullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        lists = new ArrayList<FutureWeather>();
                        break;
                    case XmlPullParser.START_TAG:
                        if(pullParser.getName().equals("weather")) {
                            futureWeather = new FutureWeather();
                        }
                        if (futureWeather != null) {
                            if (pullParser.getName().equals("date")) {
                                eventType = pullParser.next();
                                futureWeather.setFdate(pullParser.getText());
                            } else if (pullParser.getName().equals("high")) {
                                eventType = pullParser.next();
                                futureWeather.setFhigh(pullParser.getText().substring(2).trim());
                            } else if (pullParser.getName().equals("low")) {
                                eventType = pullParser.next();
                                futureWeather.setFlow(pullParser.getText().substring(2).trim());
                            } else if (pullParser.getName().equals("type") && typeCou==0) {
                                eventType = pullParser.next();
                                futureWeather.setFtype(pullParser.getText());
                                typeCou++;
                            } else if (pullParser.getName().equals("fengxiang") && fengxiangCou==0) {
                                eventType = pullParser.next();
                                futureWeather.setFfengxiang(pullParser.getText());
                                fengxiangCou++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (pullParser.getName().equals("weather")) {
                            lists.add(futureWeather);
                            futureWeather = null;
                            typeCou = 0;
                            fengxiangCou = 0;
                        }
                        break;
                    default:
                        break;
                }
                eventType = pullParser.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    void updateTodayWeather(TodayWeather todayWeather,List<FutureWeather> lists){
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
        // yestoday
        FurWeeks[0].setText(todayWeather.getFdate0());
        FurTemps[0].setText(todayWeather.getFhigh0() + "~" + todayWeather.getFlow0());
        FurClis[0].setText(todayWeather.getFtype0());
        FurWinds[0].setText(todayWeather.getFfengxiang0());
        if (todayWeather.getFtype0()!=null) {
            changeFurureWeatherImg(FurImages[0], todayWeather.getFtype0());
        }
        //future 5 day
        for (int i=0; i<lists.size(); i++) {
            FutureWeather futureWeather = lists.get(i);
            FurWeeks[i+1].setText(futureWeather.getFdate());
            FurTemps[i+1].setText(futureWeather.getFhigh() + "~" + futureWeather.getFlow());
            FurClis[i+1].setText(futureWeather.getFtype());
            FurWinds[i+1].setText(futureWeather.getFfengxiang());
            if (futureWeather.getFtype() != null) {
                changeFurureWeatherImg(FurImages[i+1], futureWeather.getFtype());
            }
        }


        if(todayWeather.getPm25()==null){}

        else if(Integer.parseInt(todayWeather.getPm25())<=50){
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
        if(todayWeather.getType()==null){}

        else if(todayWeather.getType().equals("晴")){
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
    long exitTime = 0;


    void changeFurureWeatherImg(ImageView view, String str) {
        if(str.equals("暴雪")) {
            view.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        } else if (str.equals("暴雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        } else if (str.equals("大暴雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        } else if (str.equals("大雪")) {
            view.setImageResource(R.drawable.biz_plugin_weather_daxue);
        } else if (str.equals("大雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_dayu);
        } else if (str.equals("多云")) {
            view.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        } else if (str.equals("雷阵雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        } else if (str.equals("雷阵雨冰雹")) {
            view.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        } else if (str.equals("晴")) {
            view.setImageResource(R.drawable.biz_plugin_weather_qing);
        } else if (str.equals("沙尘暴")) {
            view.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        } else if (str.equals("特大暴雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        } else if (str.equals("雾")) {
            view.setImageResource(R.drawable.biz_plugin_weather_wu);
        } else if (str.equals("小雪")) {
            view.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        } else if (str.equals("小雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        } else if (str.equals("阴")) {
            view.setImageResource(R.drawable.biz_plugin_weather_yin);
        } else if (str.equals("雨夹雪")) {
            view.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        } else if (str.equals("阵雪")) {
            view.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        } else if (str.equals("阵雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        } else if (str.equals("中雪")) {
            view.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        } else if (str.equals("中雨")) {
            view.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }
    }

    @Override

    public void onBackPressed() {
//        if ((System.currentTimeMillis() - exitTime) > 2000) {
//            // ToastUtil.makeToastInBottom("再按一次退出应用", MainMyselfActivity);
//            Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
//            exitTime = System.currentTimeMillis();
//            return;
//        }
//        finish();
        System.exit(0);

    }
}
