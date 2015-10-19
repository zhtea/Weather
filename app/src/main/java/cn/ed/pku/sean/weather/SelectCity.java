package cn.ed.pku.sean.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.ed.pku.sean.weather.db.CityDB;

/**
 * Created by Bryce on 2015/10/18.
 */
public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        Intent i =new Intent(this, CityDB.class);
        startActivity(i);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.title_back:
                finish();
                default:
                    break;
        }
    }
}
