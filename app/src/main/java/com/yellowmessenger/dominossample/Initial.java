package com.yellowmessenger.dominossample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Initial extends AppCompatActivity {

    LinearLayout dynamicLayout;
    EditText botnameView,
            botidView;
    boolean enableHistory = true;
    Switch historyEnabler;
    ArrayList<Integer> ids = new ArrayList<Integer>();

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        SharedPreferences sp = getSharedPreferences("bot_mem", MODE_PRIVATE);
        dynamicLayout = findViewById(R.id.payloadsDynamic);
        botidView = findViewById(R.id.botid);
        botnameView = findViewById(R.id.botname);
        historyEnabler = findViewById(R.id.switch1);
        historyEnabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                enableHistory = b;
            }
        });
        botnameView.setText(sp.getString("botname", ""));
        botidView.setText(sp.getString("botid", ""));
        EditText et = new EditText(this);
        et.setId(0);
        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        et.setHint("payload : value");
        dynamicLayout.addView(et);
        ids.add(0);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        intent.put
    }

    public void createBot(View view) {
        SharedPreferences sp = getSharedPreferences("bot_mem", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("botname", botnameView.getText().toString().trim());
        editor.putString("botid", botidView.getText().toString().trim());
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        HashMap<String, String> bot_details = new HashMap<String, String>();
        bot_details.put("botname", botnameView.getText().toString().trim());
        bot_details.put("botid", botidView.getText().toString().trim());
        bot_details.put("history", Boolean.toString(enableHistory));
        intent.putExtra("bot_details", bot_details);

        HashMap<String,String> dynamicContenct=new HashMap<String,String>();
        for(Integer i : ids)
        {
            TextView tempTV=dynamicLayout.findViewById(i);
            String[] s=tempTV.getText().toString().trim().split(":");
            dynamicContenct.put(s[0],s[1]);
        }
        intent.putExtra("dynamic_payload",dynamicContenct);
        startActivity(intent);
    }

    public void addDynamicView(View view) {
        int id = ids.get(ids.size() - 1)+1;
        EditText et = new EditText(this);
        et.setId(id);
        et.setHint("payload : value");
        et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dynamicLayout.addView(et);
        ids.add(id);
    }

    public void deleteView(View view) {
//        View namebar=dynamicLayout.findViewById(1);
        if(ids.size()>1)
        {
            Log.i("bot_arr",ids.toString());
            dynamicLayout.removeView(dynamicLayout.findViewById(ids.get(ids.size() - 1)));
            ids.remove(ids.size() - 1);
            Log.i("bot_arr",ids.toString());
        }
    }
}
