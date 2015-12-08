package cn.ed.pku.sean.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.ed.pku.sean.weather.app.MyApplication;
import cn.ed.pku.sean.weather.bean.City;

/**
 * Created by Bryce on 2015/10/18.
 */
public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;
    ListView mlistView;
    EditText meditText;
   // CityDB cityDB;
//    Button searchButton;
    List<City> list =new ArrayList<City>() ;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        mBackBtn = (ImageView)findViewById(R.id.title_back);
        meditText = (EditText)findViewById(R.id.city_search_bar);
        mBackBtn.setOnClickListener(this);
        mlistView = (ListView)findViewById(R.id.list_view);

       // MyApplication m = new MyApplication();
        String[] s=new String[MyApplication.mCityList.size()];
        for(int i=0;i<MyApplication.mCityList.size();i++){
            s[i]=MyApplication.mCityList.get(i).getCity().toString();
        }
        ArrayAdapter<String> adapter=new <String>ArrayAdapter(SelectCity.this,android.R.layout.simple_list_item_1,s);
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent city_intent = new Intent(SelectCity.this, MainActivity.class);
                city_intent.putExtra("city", MyApplication.mCityDB.getCityCode(parent.getItemAtPosition(position).toString()));
                //Log.e("234",parent.getItemAtPosition(position).toString());
                startActivity(city_intent);
            }
        });


       // Log.e("search","  "+meditText.getText().toString());
         TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("textwatcher","werewerew");
                MyApplication.mCityDB.searchCity(meditText.getText().toString());
                String[] s1=new String[MyApplication.mCityList.size()];
                for(int i=0;i<MyApplication.mCityList.size();i++){
                    s1[i]=MyApplication.mCityList.get(i).getCity().toString();
                }
                ArrayAdapter<String> adapter1=new <String>ArrayAdapter(SelectCity.this,android.R.layout.simple_list_item_1,s1);
                mlistView.setAdapter(adapter1);
                mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent city_intent = new Intent(SelectCity.this, MainActivity.class);
                        city_intent.putExtra("city", MyApplication.mCityDB.getCityCode(parent.getItemAtPosition(position).toString()));
                        //Log.e("234",parent.getItemAtPosition(position).toString());
                        startActivity(city_intent);
                        // Toast.makeText(SelectCity.this, "点我并没有卵用", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        meditText.addTextChangedListener(textWatcher);

    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.title_back:
                finish();
                break;
            /*case R.id.city_click_button:
                Log.e("search",meditText.getText().toString());
                Intent i =new Intent(this, CityDB.class);
                i.putExtra("search", meditText.getText().toString());
                startActivity(i);
                Intent get=this.getIntent();
                ArrayList<String> res =get.getStringArrayListExtra("res");
                ArrayAdapter<String>adapter1=new <String>ArrayAdapter(SelectCity.this,android.R.layout.simple_list_item_1,res);
                mlistView.setAdapter(adapter1);
                break;*/

                default:
                    break;
        }
    }
}
